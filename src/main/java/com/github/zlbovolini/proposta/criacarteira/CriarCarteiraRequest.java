package com.github.zlbovolini.proposta.criacarteira;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.zlbovolini.proposta.comum.Cartao;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

public class CriarCarteiraRequest {

    @NotBlank
    @Email
    private final String email;

    @JsonCreator(mode = PROPERTIES)
    CriarCarteiraRequest(String email) {
        this.email = email;
    }

    public Carteira toModel(CarteiraTipo tipo, Cartao cartao) {
        return new Carteira(email, tipo, cartao);
    }
}
