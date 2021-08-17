package com.github.zlbovolini.proposta.comum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DadosSolicitante {

    @NotBlank
    private final String documento;
    @NotBlank
    private final String nome;
    @NotNull
    private final Long idProposta;

    public DadosSolicitante(String documento, String nome, Long idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.idProposta = idProposta;
    }

    public DadosSolicitante(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getId();
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public Long getIdProposta() {
        return idProposta;
    }
}
