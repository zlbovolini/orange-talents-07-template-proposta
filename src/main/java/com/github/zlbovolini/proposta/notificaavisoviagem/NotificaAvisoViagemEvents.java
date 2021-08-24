package com.github.zlbovolini.proposta.notificaavisoviagem;

import com.github.zlbovolini.proposta.criaavisoviagem.AvisoViagem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificaAvisoViagemEvents {

    private final List<NotificaAvisoViagemEvent> notificaAvisoViagemEvents;

    public NotificaAvisoViagemEvents(List<NotificaAvisoViagemEvent> notificaAvisoViagemEvents) {
        this.notificaAvisoViagemEvents = notificaAvisoViagemEvents;
    }

    public void executa(AvisoViagem avisoViagem) {
        notificaAvisoViagemEvents.forEach(event -> event.executa(avisoViagem));
    }
}
