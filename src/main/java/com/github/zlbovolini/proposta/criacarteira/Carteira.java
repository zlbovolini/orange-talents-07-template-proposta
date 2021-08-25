package com.github.zlbovolini.proposta.criacarteira;

import com.github.zlbovolini.proposta.comum.Cartao;
import org.hibernate.annotations.Type;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // !TODO tipo binario
    @Type(type = "uuid-char")
    private UUID uuid = UUID.randomUUID();

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CarteiraTipo tipo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Cartao cartao;

    @Deprecated
    Carteira() {}

    Carteira(String email, CarteiraTipo tipo, @Valid @NotNull Cartao cartao) {
        Assert.notNull(cartao, "Cartao deve ser informado");
        Assert.isTrue(!email.isBlank(), "Email da carteira digital deve ser informado");
        this.email = email;
        this.tipo = tipo;
        this.cartao = cartao;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public CarteiraTipo getTipo() {
        return tipo;
    }

    public Cartao getCartao() {
        return cartao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Carteira carteira = (Carteira) o;

        return Objects.equals(email, carteira.email)
                && tipo == carteira.tipo
                && Objects.equals(cartao, carteira.cartao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, tipo, cartao);
    }
}
