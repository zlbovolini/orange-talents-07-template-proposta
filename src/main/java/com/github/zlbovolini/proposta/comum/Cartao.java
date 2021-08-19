package com.github.zlbovolini.proposta.comum;

import com.github.zlbovolini.proposta.criabiometria.Biometria;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String numero;

    @OneToMany(mappedBy = "cartao")
    private Set<Biometria> biometrias;

    @Deprecated
    Cartao() {}

    public Cartao(String numero) {
        this.numero = numero;
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
