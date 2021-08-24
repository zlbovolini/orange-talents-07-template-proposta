package com.github.zlbovolini.proposta.criaavisoviagem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.zlbovolini.proposta.comum.Cartao;
import com.github.zlbovolini.proposta.comum.ClientRequestInfo;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

class CriarAvisoViagemRequest {

    @NotBlank
    private final String destino;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "dd/MM/yyyy")
    private final LocalDate termino;

    @JsonCreator(mode = PROPERTIES)
    CriarAvisoViagemRequest(String destino, LocalDate termino) {
        this.destino = destino;
        this.termino = termino;
    }

    public AvisoViagem toModel(@Valid @NotNull Cartao cartao, @Valid @NotNull ClientRequestInfo clientRequestInfo) {
        return new AvisoViagem(destino, termino, clientRequestInfo.getClientIp(), clientRequestInfo.getUserAgent(), cartao);
    }
}
