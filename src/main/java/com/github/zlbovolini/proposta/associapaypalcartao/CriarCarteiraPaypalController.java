package com.github.zlbovolini.proposta.associapaypalcartao;

import com.github.zlbovolini.proposta.comum.CartaoRepository;
import com.github.zlbovolini.proposta.criacarteira.Carteira;
import com.github.zlbovolini.proposta.criacarteira.CriarCarteiraRequest;
import com.github.zlbovolini.proposta.exception.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

import static com.github.zlbovolini.proposta.criacarteira.CarteiraTipo.PAYPAL;

@RestController
@RequestMapping("/api/v1/cartoes")
public class CriarCarteiraPaypalController {

    private final CartaoRepository cartaoRepository;
    private final AssociaCarteiraPaypalService associaCarteiraPaypalService;

    public CriarCarteiraPaypalController(CartaoRepository cartaoRepository,
                                         AssociaCarteiraPaypalService associaCarteiraPaypalService) {
        this.cartaoRepository = cartaoRepository;
        this.associaCarteiraPaypalService = associaCarteiraPaypalService;
    }

    @PostMapping("/{uuid}/carteiras/paypal")
    public ResponseEntity<?> criar(@PathVariable UUID uuid,
                                   @Valid @RequestBody CriarCarteiraRequest criarCarteiraRequest) {
        return cartaoRepository.findByUuid(uuid)
                .map(cartao -> {

                    if (cartao.possuiCarteira(PAYPAL)) {
                        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                                .addGlobalError("Este cartão já está associado a uma carteira do Paypal");
                        return ResponseEntity.unprocessableEntity().body(apiErrorResponse);
                    }

                    Carteira carteira = criarCarteiraRequest.toModel(PAYPAL, cartao);
                    associaCarteiraPaypalService.executa(carteira);

                    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{uuid}")
                            .buildAndExpand(carteira.getUuid())
                            .toUri();

                    return ResponseEntity.created(location).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
