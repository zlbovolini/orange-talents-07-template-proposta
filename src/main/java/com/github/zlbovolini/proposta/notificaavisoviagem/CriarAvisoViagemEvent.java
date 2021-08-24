package com.github.zlbovolini.proposta.notificaavisoviagem;

import com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagem;

public interface NotificaAvisoViagemEvent {

    void executa(AvisoViagem avisoViagem);
}
