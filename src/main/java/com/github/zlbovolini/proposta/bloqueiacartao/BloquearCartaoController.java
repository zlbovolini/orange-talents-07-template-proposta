package com.github.zlbovolini.proposta.bloqueiacartao;

import com.github.zlbovolini.proposta.comum.Cartao;
import com.github.zlbovolini.proposta.comum.CartaoRepository;
import com.github.zlbovolini.proposta.exception.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cartoes")
public class BloquearCartaoController {

    private final CartaoRepository cartaoRepository;
    private final TransactionTemplate transactionTemplate;

    public BloquearCartaoController(CartaoRepository cartaoRepository,
                                    TransactionTemplate transactionTemplate) {
        this.cartaoRepository = cartaoRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @PostMapping("/{uuid}/bloqueios")
    public ResponseEntity<?> bloquear(@PathVariable UUID uuid, HttpServletRequest request) {

        boolean existeCartao = cartaoRepository.existsByUuid(uuid);

        if (!existeCartao) {
            return ResponseEntity.notFound().build();
        }

        return Optional.ofNullable(request.getHeader("User-Agent"))
                .map(userAgent -> {
                    String clientIp = request.getRemoteAddr();

                    boolean bloqueadoComSucesso = transactionTemplate.execute(status -> {
                        Cartao cartao = cartaoRepository.findByUuid(uuid)
                                .orElseThrow();
                        ClientRequestInfo clientRequestInfo = new ClientRequestInfo(clientIp, userAgent);

                        boolean sucesso = cartao.bloquear(clientRequestInfo);
                        cartaoRepository.save(cartao);

                        return sucesso;
                    });

                    if (bloqueadoComSucesso) {
                        return ResponseEntity.ok().build();
                    }

                    ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                            .addGlobalError("Cartão já está bloqueado");
                    return ResponseEntity.unprocessableEntity().body(apiErrorResponse);
                })
                .orElseGet(() -> {
                        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                                .addGlobalError("O Header User-Agent deve ser informado");
                        return ResponseEntity.badRequest().body(apiErrorResponse);
                });
    }
}
