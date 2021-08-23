package com.github.zlbovolini.proposta.bloqueiacartao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BloqueioCartaoEvents {

    private final List<SolicitaBloqueioCartaoEvent> solicitaBloqueioCartaoEvents;

    public BloqueioCartaoEvents(List<SolicitaBloqueioCartaoEvent> solicitaBloqueioCartaoEvents) {
        this.solicitaBloqueioCartaoEvents = solicitaBloqueioCartaoEvents;
    }

    public void execute(Bloqueio bloqueio) {
        solicitaBloqueioCartaoEvents.forEach(e -> e.executa(bloqueio));
    }
}
