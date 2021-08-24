package com.github.zlbovolini.proposta.notificaavisoviagem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagem;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NotificaAvisoViagemRequest {

    @NotBlank
    private final String destino;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate validoAte;

    public NotificaAvisoViagemRequest(AvisoViagem avisoViagem) {
        this.destino = avisoViagem.getDestino();
        this.validoAte = avisoViagem.getTermino();
    }

    public String getDestino() {
        return destino;
    }

    public String getValidoAte() {
        return validoAte.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
