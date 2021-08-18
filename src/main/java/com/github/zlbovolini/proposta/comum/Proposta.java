package com.github.zlbovolini.proposta.comum;

import com.github.zlbovolini.proposta.validation.CPFOrCNPJ;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @CPFOrCNPJ
    private String documento;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    @NotNull
    @PositiveOrZero
    private BigDecimal salario;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PropostaStatus status = PropostaStatus.RECEBIDO;

    private String numeroCartao;

    @Deprecated
    Proposta() {}

    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public void atualizaStatus(PropostaStatus status) {
        this.status = status;
    }

    public void atualizaNumeroCartao(@NotBlank String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }
}
