package com.github.zlbovolini.proposta.comum;

import com.github.zlbovolini.proposta.bloqueiacartao.Bloqueio;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// !TODO validar
public class ClientRequestInfo {

    @NotBlank
    private String clientIp;

    @NotBlank
    private String userAgent;

    public ClientRequestInfo(String clientIp, String userAgent) {
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Bloqueio toBloqueio(@Valid @NotNull Cartao cartao) {
        return new Bloqueio(cartao, clientIp, userAgent);
    }
}
