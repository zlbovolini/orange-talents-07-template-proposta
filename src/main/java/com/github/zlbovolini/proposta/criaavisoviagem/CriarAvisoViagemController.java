package com.github.zlbovolini.proposta.criaavisoviagem;

import com.github.zlbovolini.proposta.comum.CartaoRepository;
import com.github.zlbovolini.proposta.comum.ClientRequestInfo;
import com.github.zlbovolini.proposta.notificaavisoviagem.NotificaAvisoViagemEvents;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cartoes")
public class CriarAvisoViagemController {

    private final AvisoViagemRepository avisoViagemRepository;
    private final CartaoRepository cartaoRepository;
    private final TransactionTemplate transactionTemplate;
    private final NotificaAvisoViagemEvents notificaAvisoViagemEvents;

    public CriarAvisoViagemController(AvisoViagemRepository avisoViagemRepository,
                                      CartaoRepository cartaoRepository,
                                      TransactionTemplate transactionTemplate,
                                      NotificaAvisoViagemEvents notificaAvisoViagemEvents) {
        this.avisoViagemRepository = avisoViagemRepository;
        this.cartaoRepository = cartaoRepository;
        this.transactionTemplate = transactionTemplate;
        this.notificaAvisoViagemEvents = notificaAvisoViagemEvents;
    }

    @PostMapping("/{uuid}/viagens")
    public ResponseEntity<?> criar(@PathVariable UUID uuid,
                                   @Valid @RequestBody CriarAvisoViagemRequest criarAvisoViagemRequest,
                                   @RequestHeader("X-Forwarded-For") String clientIp,
                                   @RequestHeader("User-Agent") String userAgent) {
        return cartaoRepository.findByUuid(uuid)
                .map(cartao -> {

                    ClientRequestInfo clientRequestInfo = new ClientRequestInfo(clientIp, userAgent);
                    AvisoViagem avisoViagem = criarAvisoViagemRequest.toModel(cartao, clientRequestInfo);

                    transactionTemplate.execute(status -> avisoViagemRepository.save(avisoViagem));

                    notificaAvisoViagemEvents.executa(avisoViagem);

                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
