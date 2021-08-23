package com.github.zlbovolini.proposta.associacartaoproposta;

import com.github.zlbovolini.proposta.comum.Cartao;
import com.github.zlbovolini.proposta.comum.CartaoRepository;
import com.github.zlbovolini.proposta.comum.Proposta;
import com.github.zlbovolini.proposta.comum.PropostaRepository;
import feign.FeignException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import static com.github.zlbovolini.proposta.comum.PropostaStatus.ELEGIVEL;

@Component
public class AtualizaCartaoPropostasAprovadasScheduledTask {

    private final PropostaRepository propostaRepository;
    private final CartaoRepository cartaoRepository;
    private final TransactionTemplate transactionTemplate;
    private final ConsultaCartao consultaCartao;

    public AtualizaCartaoPropostasAprovadasScheduledTask(PropostaRepository propostaRepository,
                                                         CartaoRepository cartaoRepository,
                                                         TransactionTemplate transactionTemplate,
                                                         ConsultaCartao consultaCartao) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.transactionTemplate = transactionTemplate;
        this.consultaCartao = consultaCartao;
    }

    /**
     * !TODO Não garante que todos os resultados serão processados.
     * Importante: Os 1.000 (pageSize) primeiros cartões registrados serão processados,
     * mas se o status ou cartão não mudar pode ocorrer que os outros cartões nunca sejam processados.
     * Importante: Não implementar com loop ou recursão.
     */
    @Scheduled(fixedDelayString = "${scheduled-task.consulta-existe-cartao.periodicidade.executa-operacao}")
    public void executa() {

        final int pageSize = 1_000;
        Pageable pageable = PageRequest.of(0, pageSize);

        propostaRepository.findByStatusAndCartaoIsNull(ELEGIVEL, pageable)
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
        transactionTemplate.execute(status -> {
            Proposta proposta = propostaRepository.findById(propostaId)
                    .orElseThrow();

            String numero = detalhesCartao.getId();
            Assert.notNull(numero, "Recebido número de cartão inválido da API de cartoes");
            Assert.isTrue(!numero.isBlank(), "Recebido número de cartão inválido da API de cartoes");

            Cartao cartao = new Cartao(numero);

            cartaoRepository.save(cartao);
            proposta.atualizaCartao(cartao);

            return status;
        });
    }
}
