package com.github.zlbovolini.proposta.notificaavisoviagem;

import com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagem;

public interface CriarAvisoViagemEvent {

    void executa(AvisoViagem avisoViagem);
}
