package com.github.zlbovolini.proposta.notificabloqueiocartao;

import com.github.zlbovolini.proposta.bloqueiacartao.Bloqueio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CriarBloqueioCartaoEvents {

    private final List<CriarBloqueioCartaoEvent> criarBloqueioCartaoEvents;

    public CriarBloqueioCartaoEvents(List<CriarBloqueioCartaoEvent> criarBloqueioCartaoEvents) {
        this.criarBloqueioCartaoEvents = criarBloqueioCartaoEvents;
    }

    public void execute(Bloqueio bloqueio) {
        criarBloqueioCartaoEvents.forEach(e -> e.executa(bloqueio));
    }
}
