package com.github.zlbovolini.proposta.notificaavisoviagem;

import com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CriarAvisoViagemEvents {

    private final List<CriarAvisoViagemEvent> criarAvisoViagemEvents;

    public CriarAvisoViagemEvents(List<CriarAvisoViagemEvent> criarAvisoViagemEvents) {
        this.criarAvisoViagemEvents = criarAvisoViagemEvents;
    }

    public void executa(AvisoViagem avisoViagem) {
        criarAvisoViagemEvents.forEach(event -> event.executa(avisoViagem));
    }
}
