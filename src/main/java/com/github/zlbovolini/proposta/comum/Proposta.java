package com.github.zlbovolini.proposta.comum;

import com.github.zlbovolini.proposta.validation.CPFOrCNPJ;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // !TODO tipo binario
    @Type(type = "uuid-char")
    private UUID uuid = UUID.randomUUID();

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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Cartao cartao;

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

    public UUID getUuid() {
        return uuid;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public PropostaStatus getStatus() {
        return status;
    }

    public void atualizaStatus(PropostaStatus status) {
        this.status = status;
    }

    public void atualizaCartao(@Valid @NotNull Cartao cartao) {
        this.cartao = cartao;
    }
}
