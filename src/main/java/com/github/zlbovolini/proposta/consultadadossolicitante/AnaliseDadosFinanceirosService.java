package com.github.zlbovolini.proposta.consultadadossolicitante;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zlbovolini.proposta.comum.Proposta;
import com.github.zlbovolini.proposta.comum.PropostaRepository;
import com.github.zlbovolini.proposta.criaproposta.CriarPropostaEvent;
import com.github.zlbovolini.proposta.comum.DadosSolicitante;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class AnaliseDadosFinanceirosService implements CriarPropostaEvent {

    private final PropostaRepository propostaRepository;
    private final ConsultaDadosFinanceiros consultaDadosFinanceiros;
    private final PlatformTransactionManager transactionManager;

    public AnaliseDadosFinanceirosService(PropostaRepository propostaRepository,
                                          ConsultaDadosFinanceiros consultaDadosFinanceiros,
                                          PlatformTransactionManager transactionManager) {
        this.propostaRepository = propostaRepository;
        this.consultaDadosFinanceiros = consultaDadosFinanceiros;
        this.transactionManager = transactionManager;
    }

    @Override
    public void execute(DadosSolicitante dadosSolicitante) {
        AnaliseDadosFinanceirosResponse consulta;
        try {
            consulta = consultaDadosFinanceiros.consulta(dadosSolicitante);
        } catch (FeignException.UnprocessableEntity e) {
            consulta = fromJSON(e.contentUTF8());
        }
        atualizaStatusProposta(consulta);
    }

    private AnaliseDadosFinanceirosResponse fromJSON(String json) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, AnaliseDadosFinanceirosResponse.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    // !TODO validar dados da consulta, se dados do request e response sao os mesmos
    private void atualizaStatusProposta(AnaliseDadosFinanceirosResponse consulta) {
        ResultadoSolicitacao resultadoSolicitacao = consulta.getResultadoSolicitacao();

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
                Proposta proposta = propostaRepository.findById(consulta.getIdProposta())
                        .orElseThrow();
                proposta.atualizaStatus(resultadoSolicitacao.getStatus());
                // utiliza a interface funcional para implementar com lambda
                return true;
            }
        );
    }
}
