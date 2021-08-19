package com.github.zlbovolini.proposta.criabiometria;

import com.github.zlbovolini.proposta.comum.Cartao;
import com.github.zlbovolini.proposta.validation.Base64Encoded;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // !TODO tipo binario
    @Type(type = "uuid-char")
    private UUID uuid = UUID.randomUUID();

    @NotBlank
    @Base64Encoded
    private String fingerprint;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Cartao cartao;

    private Instant criadaEm = Instant.now();

    @Deprecated
    Biometria() {}

    Biometria(String fingerprint, Cartao cartao) {
        this.fingerprint = fingerprint;
        this.cartao = cartao;
    }

    UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Biometria biometria = (Biometria) o;
        return Objects.equals(fingerprint, biometria.fingerprint)
                && Objects.equals(cartao, biometria.cartao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fingerprint, cartao);
    }
}
