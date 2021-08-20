package com.github.zlbovolini.proposta.criabiometria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.zlbovolini.proposta.comum.Cartao;
import com.github.zlbovolini.proposta.validation.Base64Encoded;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

class CriarBiometriaRequest {

    @NotBlank
    @Base64Encoded
    private final String fingerprint;

    @JsonCreator(mode = PROPERTIES)
    CriarBiometriaRequest(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Biometria toModel(UUID cartaoUuid, Function<UUID, Optional<Cartao>> buscaCartao) {
        Cartao cartao = buscaCartao.apply(cartaoUuid)
                .orElseThrow();
        return new Biometria(fingerprint, cartao);
    }
}
