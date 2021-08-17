package com.github.zlbovolini.proposta.consultadadossolicitante;

// !TODO
public class AnaliseDadosFinanceirosResponse {

    private String documento;
    private String nome;
    private ResultadoSolicitacao resultadoSolicitacao;
    private Long idProposta;

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

    public Long getIdProposta() {
        return idProposta;
    }

    public void setIdProposta(Long idProposta) {
        this.idProposta = idProposta;
    }
}
