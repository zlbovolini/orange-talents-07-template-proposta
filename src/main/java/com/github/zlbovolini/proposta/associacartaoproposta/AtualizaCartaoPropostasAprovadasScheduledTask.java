package com.github.zlbovolini.proposta.associacartaoproposta;

import com.github.zlbovolini.proposta.comum.Cartao;
import com.github.zlbovolini.proposta.comum.CartaoRepository;
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
    private final CartaoRepository cartaoRepository;
    private final PlatformTransactionManager transactionManager;
    private final ConsultaCartao consultaCartao;

    public AtualizaCartaoPropostasAprovadasScheduledTask(PropostaRepository propostaRepository,
                                                         CartaoRepository cartaoRepository,
                                                         PlatformTransactionManager transactionManager,
                                                         ConsultaCartao consultaCartao) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.transactionManager = transactionManager;
        this.consultaCartao = consultaCartao;
    }

    @Scheduled(fixedDelayString = "${scheduled-task.consulta-existe-cartao.periodicidade.executa-operacao}")
    public void executa() {
        propostaRepository.findByStatusAndCartaoIsNull(ELEGIVEL)
                .forEach(proposta -> {
                    try {
                        DadosCartaoResponse detalhesCartao = consultaCartao.consulta(proposta.getUuid());
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

            String numero = detalhesCartao.getId();
            Assert.notNull(numero, "Recebido número de cartão inválido da API de cartoes");
            Assert.isTrue(!numero.isBlank(), "Recebido número de cartão inválido da API de cartoes");

            Cartao cartao = new Cartao(numero);

            cartaoRepository.save(cartao);
            proposta.atualizaCartao(cartao);
            // utiliza a interface funcional para implementar com lambda
            return true;
        });
    }
}
