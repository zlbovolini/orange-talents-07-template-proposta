package com.github.zlbovolini.proposta.associacartaoproposta;

import com.github.zlbovolini.proposta.comum.Proposta;
import com.github.zlbovolini.proposta.comum.PropostaRepository;
import feign.FeignException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import static com.github.zlbovolini.proposta.comum.PropostaStatus.ELEGIVEL;

@Component
public class AtualizaCartaoPropostasAprovadasScheduledTask {

    private final PropostaRepository propostaRepository;
    private final PlatformTransactionManager transactionManager;
    private final ConsultaCartao consultaCartao;

    public AtualizaCartaoPropostasAprovadasScheduledTask(PropostaRepository propostaRepository, PlatformTransactionManager transactionManager, ConsultaCartao consultaCartao) {
        this.propostaRepository = propostaRepository;
        this.transactionManager = transactionManager;
        this.consultaCartao = consultaCartao;
    }

    @Scheduled(fixedDelayString = "${scheduled-task.consulta-existe-cartao.periodicidade.executa-operacao}")
    public void executa() {
        propostaRepository.findByStatusAndNumeroCartaoIsNull(ELEGIVEL)
                .forEach(proposta -> {
                    try {
                        DadosCartaoResponse detalhesCartao = consultaCartao.consulta(proposta.getIdentificador());
                        atualizaCartaoProposta(proposta.getId(), detalhesCartao);
                    } catch (FeignException.FeignClientException | FeignException.FeignServerException e) {
                        // ignores exceptions
                    }
                });
    }

    private void atualizaCartaoProposta(Long propostaId, DadosCartaoResponse detalhesCartao) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
                    Proposta proposta = propostaRepository.findById(propostaId)
                            .orElseThrow();
                    Assert.notNull(detalhesCartao.getId(), "Recebido número de cartão inválido da API de cartoes");
                    proposta.atualizaNumeroCartao(detalhesCartao.getId());
                    // utiliza a interface funcional para implementar com lambda
                    return true;
                }
        );
    }
}
