package com.github.zlbovolini.proposta.comum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class DadosSolicitante {

    @NotBlank
    private final String documento;
    @NotBlank
    private final String nome;
    @NotNull
    private final UUID idProposta;

    public DadosSolicitante(String documento, String nome, UUID uuid) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = uuid;
    }

    public DadosSolicitante(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getUuid();
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public UUID getIdProposta() {
        return idProposta;
    }
}
