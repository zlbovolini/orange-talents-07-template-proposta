package com.github.zlbovolini.proposta.bloqueiacartao;

import com.github.zlbovolini.proposta.comum.CartaoRepository;
import com.github.zlbovolini.proposta.comum.ClientRequestInfo;
import com.github.zlbovolini.proposta.comum.PropostaRepository;
import com.github.zlbovolini.proposta.exception.ApiErrorException;
import com.github.zlbovolini.proposta.exception.ApiErrorResponse;
import com.github.zlbovolini.proposta.notificabloqueiocartao.CriarBloqueioCartaoEvents;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cartoes")
public class CriarBloqueioCartaoController {

    private final CartaoRepository cartaoRepository;
    private final PropostaRepository propostaRepository;
    private final BloqueioRepository bloqueioRepository;
    private final TransactionTemplate transactionTemplate;
    private final CriarBloqueioCartaoEvents criarBloqueioCartaoEvents;

    public CriarBloqueioCartaoController(CartaoRepository cartaoRepository,
                                         PropostaRepository propostaRepository, BloqueioRepository bloqueioRepository, TransactionTemplate transactionTemplate,
                                         CriarBloqueioCartaoEvents criarBloqueioCartaoEvents) {
        this.cartaoRepository = cartaoRepository;
        this.propostaRepository = propostaRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.transactionTemplate = transactionTemplate;
        this.criarBloqueioCartaoEvents = criarBloqueioCartaoEvents;
    }

    @PostMapping("/{uuid}/bloqueios")
    public ResponseEntity<?> criar(@PathVariable UUID uuid,
                                   @RequestHeader("X-Forwarded-For") String clientIp,
                                   @RequestHeader("User-Agent") String userAgent,
                                   @AuthenticationPrincipal OidcUser usuarioLogado) {

        String usuarioLogadoEmail = usuarioLogado.getEmail();
        ownerOrFail(uuid, usuarioLogadoEmail);

        return cartaoRepository.findByUuid(uuid)
                .map(cartao -> {

                    if (cartao.possuiBloqueio()) {
                        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                                .addGlobalError("Cartão está em processo de bloqueio ou já está bloqueado");
                        return ResponseEntity.unprocessableEntity().body(apiErrorResponse);
                    }

                    ClientRequestInfo clientRequestInfo = new ClientRequestInfo(clientIp, userAgent);
                    Bloqueio bloqueio = clientRequestInfo.toBloqueio(cartao);

                    transactionTemplate.execute(status -> bloqueioRepository.save(bloqueio));

                    criarBloqueioCartaoEvents.execute(bloqueio);

                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void ownerOrFail(UUID uuid, String usuarioLogadoEmail) {
        propostaRepository.findEmailByCartaoUuid(uuid)
                .ifPresentOrElse(donoCartaoEmail -> {
                    boolean isOwner = donoCartaoEmail.equalsIgnoreCase(usuarioLogadoEmail);

                    if (!isOwner) {
                        throw new ApiErrorException(HttpStatus.FORBIDDEN, "Usuário não possui permissão para realizar esta operação");
                    }
                }, () -> { throw new ApiErrorException(HttpStatus.NOT_FOUND, "Cartão não encontrado"); });
    }
}
