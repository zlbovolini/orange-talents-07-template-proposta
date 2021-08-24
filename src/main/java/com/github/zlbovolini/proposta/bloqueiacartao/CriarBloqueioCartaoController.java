package com.github.zlbovolini.proposta.bloqueiacartao;

import com.github.zlbovolini.proposta.comum.CartaoRepository;
import com.github.zlbovolini.proposta.comum.ClientRequestInfo;
import com.github.zlbovolini.proposta.exception.ApiErrorResponse;
import com.github.zlbovolini.proposta.notificabloqueiocartao.CriarBloqueioCartaoEvents;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cartoes")
public class CriarBloqueioCartaoController {

    private final CartaoRepository cartaoRepository;
    private final BloqueioRepository bloqueioRepository;
    private final TransactionTemplate transactionTemplate;
    private final CriarBloqueioCartaoEvents criarBloqueioCartaoEvents;

    public CriarBloqueioCartaoController(CartaoRepository cartaoRepository,
                                         BloqueioRepository bloqueioRepository, TransactionTemplate transactionTemplate,
                                         CriarBloqueioCartaoEvents criarBloqueioCartaoEvents) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.transactionTemplate = transactionTemplate;
        this.criarBloqueioCartaoEvents = criarBloqueioCartaoEvents;
    }

    @PostMapping("/{uuid}/bloqueios")
    public ResponseEntity<?> criar(@PathVariable UUID uuid,
                                   @RequestHeader("X-Forwarded-For") String clientIp,
                                   @RequestHeader("User-Agent") String userAgent) {
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
}
