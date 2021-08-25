package com.github.zlbovolini.proposta.associapaypalcartao;

import com.github.zlbovolini.proposta.criacarteira.Carteira;
import com.github.zlbovolini.proposta.criacarteira.CarteiraTipo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

class AssociaCarteiraCartaoRequest {

    @NotBlank
    private final String email;

    @NotNull
    private final CarteiraTipo carteira;

    AssociaCarteiraCartaoRequest(Carteira carteira) {
        this.email = carteira.getEmail();
        this.carteira = carteira.getTipo();
    }

    public String getEmail() {
        return email;
    }

    public CarteiraTipo getCarteira() {
        return carteira;
    }
}
