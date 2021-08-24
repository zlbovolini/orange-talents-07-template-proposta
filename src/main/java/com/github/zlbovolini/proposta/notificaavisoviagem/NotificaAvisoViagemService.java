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

import static com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagemStatus.AGUARDANDO;
import static com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagemStatus.CRIADO;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotificaAvisoViagemService implements NotificaAvisoViagemEvent {

    private final NotificaAvisoViagem notificaAvisoViagem;
    private final AvisoViagemRepository avisoViagemRepository;
    private final TransactionTemplate transactionTemplate;

    public NotificaAvisoViagemService(NotificaAvisoViagem notificaAvisoViagem,
                                      AvisoViagemRepository avisoViagemRepository,
                                      TransactionTemplate transactionTemplate) {
        this.notificaAvisoViagem = notificaAvisoViagem;
        this.avisoViagemRepository = avisoViagemRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void executa(AvisoViagem avisoViagem) {

        String numeroCartao = avisoViagem.getCartao().getNumero();
        NotificaAvisoViagemRequest request = new NotificaAvisoViagemRequest(avisoViagem);

        transactionTemplate.execute(status -> {
            avisoViagemRepository.findById(avisoViagem.getId())
                    .ifPresentOrElse(e -> {
                        Assert.isTrue(CRIADO.equals(avisoViagem.getStatus()), "Status inválido");
                        avisoViagem.setStatus(AGUARDANDO);
                    }, () -> {
                        throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível adicionar o aviso de viagem");
                    });
            return avisoViagemRepository.save(avisoViagem);
        });

        try {
            notificaAvisoViagem.notificar(numeroCartao, request);
        } catch (FeignException.FeignClientException | FeignException.FeignServerException e) {
            // !TODO 422 - Aviso de viagem é único no sistema legado
            removerAvisoViagem(avisoViagem.getId());
            throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível adicionar o aviso de viagem");
        } catch (FeignException ex) {
            // ignora outras exceções como erro ao decodificar a resposta
        }
    }

    private void removerAvisoViagem(Long id) {
        transactionTemplate.execute(status -> {
            avisoViagemRepository.deleteById(id);
            return status;
        });
    }
}
