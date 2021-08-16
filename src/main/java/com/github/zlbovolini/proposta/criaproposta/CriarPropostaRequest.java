package com.github.zlbovolini.proposta.criaproposta;

import com.github.zlbovolini.proposta.validation.CPFOrCNPJ;

import javax.validation.constraints.*;
import java.math.BigDecimal;

class CriarPropostaRequest {

    @NotBlank
    @CPFOrCNPJ
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
        return new Proposta(documento, email, nome, endereco, salario);
    }
}
