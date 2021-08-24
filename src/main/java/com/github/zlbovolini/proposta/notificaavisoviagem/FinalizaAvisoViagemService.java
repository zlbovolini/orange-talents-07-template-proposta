package com.github.zlbovolini.proposta.notificaavisoviagem;

import com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagem;
import com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagemRepository;
import com.github.zlbovolini.proposta.exception.ApiErrorException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import static com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagemStatus.AGUARDANDO;
import static com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagemStatus.FINALIZADO;

@Service
@Order(Ordered.LOWEST_PRECEDENCE)
public class FinalizaAvisoViagemService implements CriarAvisoViagemEvent {

    private final AvisoViagemRepository avisoViagemRepository;
    private final TransactionTemplate transactionTemplate;

    public FinalizaAvisoViagemService(AvisoViagemRepository avisoViagemRepository,
                                      TransactionTemplate transactionTemplate) {
        this.avisoViagemRepository = avisoViagemRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void executa(AvisoViagem avisoViagem) {
        transactionTemplate.execute(status -> {
            avisoViagemRepository.findById(avisoViagem.getId())
                    .ifPresentOrElse(e -> {
                        Assert.isTrue(AGUARDANDO.equals(avisoViagem.getStatus()), "Status inválido");
                        avisoViagem.setStatus(FINALIZADO);
                    }, () -> {
                        throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível adicionar o aviso de viagem");
                    });
            return avisoViagemRepository.save(avisoViagem);
        });
    }
}
