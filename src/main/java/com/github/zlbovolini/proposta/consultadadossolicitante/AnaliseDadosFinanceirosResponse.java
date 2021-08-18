package com.github.zlbovolini.proposta.consultadadossolicitante;

import java.util.UUID;

// !TODO
public class AnaliseDadosFinanceirosResponse {

    private String documento;
    private String nome;
    private ResultadoSolicitacao resultadoSolicitacao;
    // identificador
    private UUID idProposta;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ResultadoSolicitacao getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    public void setResultadoSolicitacao(ResultadoSolicitacao resultadoSolicitacao) {
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    public UUID getIdProposta() {
        return idProposta;
    }

    public void setIdProposta(UUID idProposta) {
        this.idProposta = idProposta;
    }
}
