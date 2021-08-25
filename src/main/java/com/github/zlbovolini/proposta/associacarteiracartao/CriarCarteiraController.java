package com.github.zlbovolini.proposta.associacarteiracartao;

import com.github.zlbovolini.proposta.comum.CartaoRepository;
import com.github.zlbovolini.proposta.criacarteira.Carteira;
import com.github.zlbovolini.proposta.criacarteira.CarteiraTipo;
import com.github.zlbovolini.proposta.criacarteira.CriarCarteiraRequest;
import com.github.zlbovolini.proposta.exception.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

import static com.github.zlbovolini.proposta.criacarteira.CarteiraTipo.PAYPAL;
import static com.github.zlbovolini.proposta.criacarteira.CarteiraTipo.SAMSUNGPAY;

@RestController
@RequestMapping("/api/v1/cartoes")
public class CriarCarteiraController {

    private final Logger logger = LoggerFactory.getLogger(CriarCarteiraController.class);

    private final CartaoRepository cartaoRepository;
    private final AssociaCarteiraService associaCarteiraService;

    public CriarCarteiraController(CartaoRepository cartaoRepository,
                                   AssociaCarteiraService associaCarteiraService) {
        this.cartaoRepository = cartaoRepository;
        this.associaCarteiraService = associaCarteiraService;
    }

    @PostMapping("/{uuid}/carteiras/paypal")
    public ResponseEntity<?> criarPaypal(@PathVariable UUID uuid,
                                         @Valid @RequestBody CriarCarteiraRequest criarCarteiraRequest) {
        return criar(uuid, criarCarteiraRequest, PAYPAL);
    }

    @PostMapping("/{uuid}/carteiras/samsungpay")
    public ResponseEntity<?> criarSamsungPay(@PathVariable UUID uuid,
                                             @Valid @RequestBody CriarCarteiraRequest criarCarteiraRequest) {
        return criar(uuid, criarCarteiraRequest, SAMSUNGPAY);
    }

    private ResponseEntity<?> criar(UUID uuid, CriarCarteiraRequest criarCarteiraRequest, CarteiraTipo carteiraTipo) {
        return cartaoRepository.findByUuid(uuid)
                .map(cartao -> {

                    if (cartao.possuiCarteira(carteiraTipo)) {
                        logger.info("Carteira tipo={} não associada ao Cartão uuid={}, pois já possui carteira do mesmo tipo associada ", carteiraTipo, uuid);
                        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                                .addGlobalError("Este cartão já está associado a uma carteira do " + carteiraTipo);
                        return ResponseEntity.unprocessableEntity().body(apiErrorResponse);
                    }

                    Carteira carteira = criarCarteiraRequest.toModel(carteiraTipo, cartao);
                    associaCarteiraService.executa(carteira);

                    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{uuid}")
                            .buildAndExpand(carteira.getUuid())
                            .toUri();

                    logger.info("Carteira tipo={} associada ao Cartão uuid={} com sucesso!", carteiraTipo, uuid);

                    return ResponseEntity.created(location).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
