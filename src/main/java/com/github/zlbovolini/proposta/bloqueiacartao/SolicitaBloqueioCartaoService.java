package com.github.zlbovolini.proposta.bloqueiacartao;

import com.github.zlbovolini.proposta.exception.ApiErrorException;
import feign.FeignException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;

@Service
@Order(1)
public class SolicitaBloqueioCartaoService implements SolicitaBloqueioCartaoEvent {

    private final SolicitaBloqueioCartao solicitaBloqueioCartao;
    private final BloqueioRepository bloqueioRepository;
    private final TransactionTemplate transactionTemplate;

    public SolicitaBloqueioCartaoService(SolicitaBloqueioCartao solicitaBloqueioCartao,
                                         BloqueioRepository bloqueioRepository, TransactionTemplate transactionTemplate1) {
        this.solicitaBloqueioCartao = solicitaBloqueioCartao;
        this.bloqueioRepository = bloqueioRepository;
        this.transactionTemplate = transactionTemplate1;
    }

    @Override
    public void executa(Bloqueio bloqueio) {

        transactionTemplate.execute(status -> {
            bloqueioRepository.findById(bloqueio.getId())
                    .ifPresentOrElse(Bloqueio::inicia, () -> {
                        throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível bloquear o cartão");
                    });
            return bloqueioRepository.save(bloqueio);
        });

        String numero = bloqueio.getCartao().getNumero();

        try {
            // !TODO
            solicitaBloqueioCartao.bloquear(numero, Map.of("sistemaResponsavel", "proposta"));
        } catch (FeignException.FeignClientException | FeignException.FeignServerException e) {
            removerBloqueio(bloqueio.getId());
            throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível bloquear o cartão");
        }
    }

    private void removerBloqueio(Long id) {
        transactionTemplate.execute(status -> {
            bloqueioRepository.deleteById(id);
            return status;
        });
    }
}
