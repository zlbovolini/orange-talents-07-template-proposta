package com.github.zlbovolini.proposta.notificaavisoviagem;

import com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagem;
import com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagemRepository;
import com.github.zlbovolini.proposta.exception.ApiErrorException;
import feign.FeignException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagemStatus.AGUARDANDO;
import static com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagemStatus.INICIADO;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotificaAvisoViagemService implements CriarAvisoViagemEvent {

    private final NotificaAvisoViagem notificaAvisoViagem;
    private final AvisoViagemRepository avisoViagemRepository;
    private final TransactionTemplate transactionTemplate;

    private final String erro = "Não foi possível adicionar o aviso de viagem";

    public NotificaAvisoViagemService(NotificaAvisoViagem notificaAvisoViagem,
                                      AvisoViagemRepository avisoViagemRepository,
                                      TransactionTemplate transactionTemplate) {
        this.notificaAvisoViagem = notificaAvisoViagem;
        this.avisoViagemRepository = avisoViagemRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void executa(@Valid @NotNull AvisoViagem avisoViagem) {

        String numeroCartao = avisoViagem.getCartao().getNumero();
        NotificaAvisoViagemRequest request = new NotificaAvisoViagemRequest(avisoViagem);

        atualizaStatusAvisoViagemParaAguardando(avisoViagem);

        try {
            notificaAvisoViagem.notificar(numeroCartao, request);
        } catch (FeignException.FeignClientException fce) {
            removerAvisoViagem(avisoViagem.getId());
            HttpStatus status = Optional.ofNullable(HttpStatus.resolve(fce.status()))
                    .orElse(HttpStatus.BAD_REQUEST);
            throw new ApiErrorException(status, erro);
        } catch (FeignException.FeignServerException fse) {
            removerAvisoViagem(avisoViagem.getId());
            HttpStatus status = Optional.ofNullable(HttpStatus.resolve(fse.status()))
                    .orElse(HttpStatus.SERVICE_UNAVAILABLE);
            throw new ApiErrorException(status, erro);
        } catch (FeignException fe) {
            HttpStatus status = Optional.ofNullable(HttpStatus.resolve(fe.status()))
                    .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
            if (!status.is2xxSuccessful()) {
                removerAvisoViagem(avisoViagem.getId());
                throw new ApiErrorException(status, erro);
            }
        }
    }

    private void atualizaStatusAvisoViagemParaAguardando(AvisoViagem avisoViagem) {
        transactionTemplate.execute(status -> {
            avisoViagemRepository.findById(avisoViagem.getId())
                    .ifPresentOrElse(e -> {
                        Assert.isTrue(INICIADO.equals(avisoViagem.getStatus()), "Status inválido");
                        avisoViagem.setStatus(AGUARDANDO);
                    }, () -> {
                        throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, erro);
                    });
            return avisoViagemRepository.save(avisoViagem);
        });
    }

    private void removerAvisoViagem(Long id) {
        transactionTemplate.execute(status -> {
            avisoViagemRepository.deleteById(id);
            return status;
        });
    }
}
