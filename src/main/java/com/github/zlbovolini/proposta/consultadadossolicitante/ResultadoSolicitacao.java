package com.github.zlbovolini.proposta.consultadadossolicitante;

import com.github.zlbovolini.proposta.comum.PropostaStatus;

public enum ResultadoSolicitacao {

    SEM_RESTRICAO(PropostaStatus.ELEGIVEL),
    COM_RESTRICAO(PropostaStatus.NAO_ELEGIVEL);

    private final PropostaStatus status;

    ResultadoSolicitacao(PropostaStatus status) {
        this.status = status;
    }

    public PropostaStatus getStatus() {
        return status;
    }
}
