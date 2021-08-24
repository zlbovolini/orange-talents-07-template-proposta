package com.github.zlbovolini.proposta.notificabloqueiocartao;

import com.github.zlbovolini.proposta.bloqueiacartao.Bloqueio;

public interface CriarBloqueioCartaoEvent {

    void executa(Bloqueio bloqueio);
}
