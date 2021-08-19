package com.github.zlbovolini.proposta.criabiometria;

import com.github.zlbovolini.proposta.comum.CartaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/biometria")
public class CriarBiometriaController {

    private final BiometriaRepository biometriaRepository;
    private final CartaoRepository cartaoRepository;
    private final PlatformTransactionManager transactionManager;

    public CriarBiometriaController(BiometriaRepository biometriaRepository, CartaoRepository cartaoRepository, PlatformTransactionManager transactionManager) {
        this.biometriaRepository = biometriaRepository;
        this.cartaoRepository = cartaoRepository;
        this.transactionManager = transactionManager;
    }

    @PostMapping("/{numeroCartao}")
    public ResponseEntity<Void> criar(@PathVariable String numeroCartao,
                                      @Valid @RequestBody CriarBiometriaRequest criarBiometriaRequest) {

        Assert.notNull(numeroCartao, "Numero do cartão não pode ser nulo");
        Assert.isTrue(!numeroCartao.isBlank(), "Numero do cartão inválido");

        Biometria biometria = criarBiometriaRequest.toModel(numeroCartao, cartaoRepository::findByNumeroAndNumeroIsNotNull);

        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> biometriaRepository.save(biometria));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(biometria.getUuid())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
