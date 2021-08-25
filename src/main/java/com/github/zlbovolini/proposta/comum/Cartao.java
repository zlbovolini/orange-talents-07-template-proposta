package com.github.zlbovolini.proposta.comum;

import com.github.zlbovolini.proposta.bloqueiacartao.Bloqueio;
import com.github.zlbovolini.proposta.criabiometria.Biometria;
import com.github.zlbovolini.proposta.criacarteira.Carteira;
import com.github.zlbovolini.proposta.criacarteira.CarteiraTipo;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // !TODO tipo binario
    @Type(type = "uuid-char")
    private UUID uuid = UUID.randomUUID();

    @NotBlank
    private String numero;

    @OneToOne(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private Bloqueio bloqueio;

    @OneToMany(mappedBy = "cartao")
    private Set<Biometria> biometrias;

    @OneToMany(mappedBy = "cartao")
    private Set<Carteira> carteiras;

    @Deprecated
    Cartao() {}

    public Cartao(String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    public boolean possuiBloqueio() {
        return Objects.nonNull(bloqueio);
    }

    public boolean possuiCarteira(CarteiraTipo tipo) {
        return carteiras.stream()
                .anyMatch(carteira -> carteira.getTipo().equals(tipo));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Cartao cartao = (Cartao) o;
        return Objects.equals(numero, cartao.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}
