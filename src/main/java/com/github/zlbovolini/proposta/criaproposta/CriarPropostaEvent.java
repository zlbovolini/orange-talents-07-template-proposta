package com.github.zlbovolini.proposta.criaproposta;

import com.github.zlbovolini.proposta.comum.DadosSolicitante;

public interface CriarPropostaEvent {

    void execute(DadosSolicitante dadosSolicitante);
}
