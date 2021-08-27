package com.github.zlbovolini.proposta.criaproposta;

import com.github.zlbovolini.proposta.comum.Dado;
import com.github.zlbovolini.proposta.comum.Proposta;
import com.github.zlbovolini.proposta.validation.CPFOrCNPJ;
import com.github.zlbovolini.proposta.validation.UniqueHash;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

class CriarPropostaRequest {

    @NotBlank
    @CPFOrCNPJ
    @UniqueHash(entity = Proposta.class, field = "documentoHash", self = "documento")
    private final String documento;

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String nome;

    @NotBlank
    private final String endereco;

    @NotNull
    @PositiveOrZero
    private final BigDecimal salario;

    CriarPropostaRequest(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    Proposta toModel() {
        Dado documento = Dado.encode(this.documento);
        return new Proposta(documento, email, nome, endereco, salario);
    }
}
