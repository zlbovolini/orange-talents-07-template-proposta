package com.github.zlbovolini.proposta.criaavisoviagem;

import com.github.zlbovolini.proposta.comum.Cartao;
import org.hibernate.annotations.Type;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public
class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // !TODO tipo binario
    @Type(type = "uuid-char")
    private UUID uuid = UUID.randomUUID();

    @NotBlank
    private String destino;

    @NotNull
    @FutureOrPresent
    private LocalDate termino;

    private Instant criadoEm = Instant.now();

    private String clientIp;

    private String clientUserAgent;

    @Enumerated(EnumType.STRING)
    private AvisoViagemStatus status = AvisoViagemStatus.INICIADO;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Cartao cartao;

    @Deprecated
    AvisoViagem() {}

    AvisoViagem(String destino, LocalDate termino, String clientIp, String clientUserAgent,
                @Valid @NotNull Cartao cartao) {
        Assert.notNull(cartao, "O Cartão deve ser informado no cadastro de aviso de viagem");
        this.destino = destino;
        this.termino = termino;
        this.clientIp = clientIp;
        this.clientUserAgent = clientUserAgent;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getTermino() {
        return termino;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public AvisoViagemStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull AvisoViagemStatus status) {
        this.status = status;
    }
}
