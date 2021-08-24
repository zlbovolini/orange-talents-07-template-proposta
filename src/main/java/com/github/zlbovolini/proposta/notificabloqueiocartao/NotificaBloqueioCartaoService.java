package com.github.zlbovolini.proposta.notificabloqueiocartao;

import com.github.zlbovolini.proposta.bloqueiacartao.Bloqueio;
import com.github.zlbovolini.proposta.bloqueiacartao.BloqueioRepository;
import com.github.zlbovolini.proposta.exception.ApiErrorException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Optional;

import static com.github.zlbovolini.proposta.bloqueiacartao.BloqueioStatus.AGUARDANDO;
import static com.github.zlbovolini.proposta.bloqueiacartao.BloqueioStatus.INICIADO;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotificaBloqueioCartaoService implements CriarBloqueioCartaoEvent {

    @Value("${spring.application.name:Proposta API}")
    private String nomeAplicacao;

    private final NotificaBloqueioCartao notificaBloqueioCartao;
    private final BloqueioRepository bloqueioRepository;
    private final TransactionTemplate transactionTemplate;

    public NotificaBloqueioCartaoService(NotificaBloqueioCartao notificaBloqueioCartao,
                                         BloqueioRepository bloqueioRepository, TransactionTemplate transactionTemplate1) {
        this.notificaBloqueioCartao = notificaBloqueioCartao;
        this.bloqueioRepository = bloqueioRepository;
        this.transactionTemplate = transactionTemplate1;
    }

    @Override
    public void executa(Bloqueio bloqueio) {

        transactionTemplate.execute(status -> {
            bloqueioRepository.findById(bloqueio.getId())
                    .ifPresentOrElse(e -> {
                        Assert.isTrue(INICIADO.equals(bloqueio.getStatus()), "Status inválido");
                        bloqueio.setStatus(AGUARDANDO);
                    }, () -> {
                        throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível bloquear o cartão");
                    });
            return bloqueioRepository.save(bloqueio);
        });

        String numero = bloqueio.getCartao().getNumero();

        try {
            Map<String, String> request = Map.of("sistemaResponsavel", nomeAplicacao);
            notificaBloqueioCartao.notificar(numero, request);
        } catch (FeignException.FeignClientException fce) {
            removerBloqueio(bloqueio.getId());
            HttpStatus status = Optional.ofNullable(HttpStatus.resolve(fce.status()))
                    .orElse(HttpStatus.BAD_REQUEST);
            throw new ApiErrorException(status, "Não foi possível bloquear o cartão");
        } catch (FeignException.FeignServerException fse) {
            removerBloqueio(bloqueio.getId());
            HttpStatus status = Optional.ofNullable(HttpStatus.resolve(fse.status()))
                    .orElse(HttpStatus.SERVICE_UNAVAILABLE);
            throw new ApiErrorException(status, "Não foi possível bloquear o cartão");
        } catch (FeignException ex) {
            // ignora outras exceções como erro ao decodificar a resposta
        }
    }

    private void removerBloqueio(Long id) {
        transactionTemplate.execute(status -> {
            bloqueioRepository.deleteById(id);
            return status;
        });
    }
}
