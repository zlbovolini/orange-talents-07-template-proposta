package com.github.zlbovolini.proposta.bloqueiacartao;

import com.github.zlbovolini.proposta.comum.Cartao;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
public class Bloqueio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    private Cartao cartao;

    private Instant bloqueadoEm = Instant.now();

    @NotBlank
    private String clientIp;

    @NotBlank
    private String userAgent;

    @Enumerated(EnumType.STRING)
    private BloqueioStatus status = BloqueioStatus.INICIADO;

    @Deprecated
    Bloqueio() {}

    public Bloqueio(@Valid @NotNull Cartao cartao, @NotBlank String clientIp, @NotBlank String userAgent) {
        Assert.notNull(cartao, "Cartao a ser bloqueado deve ser informado");
        Assert.isTrue(!clientIp.isBlank(), "Endereço IP do cliente que realizou a requisição deve ser informado");
        Assert.isTrue(!userAgent.isBlank(), "User Agent do cliente que realizou a requisição deve ser informado");
        this.cartao = cartao;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public BloqueioStatus getStatus() {
        return status;
    }

    public void setStatus(BloqueioStatus status) {
        this.status = status;
    }
}
