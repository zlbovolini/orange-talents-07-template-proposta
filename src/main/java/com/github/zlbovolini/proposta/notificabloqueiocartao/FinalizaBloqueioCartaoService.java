package com.github.zlbovolini.proposta.notificabloqueiocartao;

import com.github.zlbovolini.proposta.bloqueiacartao.Bloqueio;
import com.github.zlbovolini.proposta.bloqueiacartao.BloqueioRepository;
import com.github.zlbovolini.proposta.exception.ApiErrorException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import static com.github.zlbovolini.proposta.bloqueiacartao.BloqueioStatus.AGUARDANDO;
import static com.github.zlbovolini.proposta.bloqueiacartao.BloqueioStatus.FINALIZADO;

@Service
@Order(Ordered.LOWEST_PRECEDENCE)
public class FinalizaBloqueioCartaoService implements CriarBloqueioCartaoEvent {

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
                    .ifPresentOrElse(e -> {
                        Assert.isTrue(AGUARDANDO.equals(bloqueio.getStatus()), "Status inválido");
                        bloqueio.setStatus(FINALIZADO);
                    }, () -> {
                        throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível bloquear o cartão");
                    });
            return bloqueioRepository.save(bloqueio);
        });
    }
}
