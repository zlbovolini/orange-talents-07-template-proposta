package com.github.zlbovolini.proposta.bloqueiacartao;

import com.github.zlbovolini.proposta.exception.ApiErrorException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@Order(2)
public class FinalizaBloqueioCartaoService implements SolicitaBloqueioCartaoEvent {

    private final BloqueioRepository bloqueioRepository;
    private final TransactionTemplate transactionTemplate;

    public FinalizaBloqueioCartaoService(BloqueioRepository bloqueioRepository, TransactionTemplate transactionTemplate) {
        this.bloqueioRepository = bloqueioRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void executa(Bloqueio bloqueio) {
        transactionTemplate.execute(status -> {
            bloqueioRepository.findById(bloqueio.getId())
                    .ifPresentOrElse(Bloqueio::finaliza, () -> {
                        throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível bloquear o cartão");
                    });
            return bloqueioRepository.save(bloqueio);
        });
    }
}
