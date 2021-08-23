package com.github.zlbovolini.proposta.bloqueiacartao;

import com.github.zlbovolini.proposta.comum.Cartao;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// !TODO validar
public class ClientRequestInfo {

    @NotBlank
    private String clientIp;

    @NotBlank
    private String userAgent;

    ClientRequestInfo(String clientIp, String userAgent) {
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }

    public Bloqueio toBloqueio(@Valid @NotNull Cartao cartao) {
        return new Bloqueio(cartao, clientIp, userAgent);
    }
}
