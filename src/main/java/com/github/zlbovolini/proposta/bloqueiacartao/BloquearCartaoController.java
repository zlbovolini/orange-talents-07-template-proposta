package com.github.zlbovolini.proposta.bloqueiacartao;

import com.github.zlbovolini.proposta.comum.CartaoRepository;
import com.github.zlbovolini.proposta.comum.ClientRequestInfo;
import com.github.zlbovolini.proposta.exception.ApiErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cartoes")
public class BloquearCartaoController {

    private final CartaoRepository cartaoRepository;
    private final BloqueioRepository bloqueioRepository;
    private final TransactionTemplate transactionTemplate;
    private final BloqueioCartaoEvents bloqueioCartaoEvents;

    public BloquearCartaoController(CartaoRepository cartaoRepository,
                                    BloqueioRepository bloqueioRepository, TransactionTemplate transactionTemplate,
                                    BloqueioCartaoEvents bloqueioCartaoEvents) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioRepository = bloqueioRepository;
        this.transactionTemplate = transactionTemplate;
        this.bloqueioCartaoEvents = bloqueioCartaoEvents;
    }

    @PostMapping("/{uuid}/bloqueios")
    public ResponseEntity<?> bloquear(@PathVariable UUID uuid,
                                      @RequestHeader("X-Forwarded-For") String clientIp,
                                      @RequestHeader("User-Agent") String userAgent) {

        ClientRequestInfo clientRequestInfo = new ClientRequestInfo(clientIp, userAgent);

        Bloqueio bloqueio = transactionTemplate.execute(status -> cartaoRepository.findByUuid(uuid)
                .map(cartao -> {

                    if (cartao.possuiBloqueio()) {
                        throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Cartão está em processo de bloqueio ou já está bloqueado");
                    }

                    Bloqueio novoBloqueio = clientRequestInfo.toBloqueio(cartao);

                    return bloqueioRepository.save(novoBloqueio);
                })
                .orElseThrow(() -> new ApiErrorException(HttpStatus.BAD_REQUEST, "Cartão não encontrado")));

        bloqueioCartaoEvents.execute(bloqueio);

        return ResponseEntity.ok().build();
    }
}
