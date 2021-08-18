package com.github.zlbovolini.proposta.acompanhaproposta;

import com.github.zlbovolini.proposta.comum.Proposta;

class AcompanhaPropostaResponse {

    private final String status;

    AcompanhaPropostaResponse(Proposta proposta) {
        status = proposta.getStatus().toString();
    }

    public String getStatus() {
        return status;
    }
}
