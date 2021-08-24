package com.github.zlbovolini.proposta.notificaavisoviagem;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "aviso-viagem", url = "${cartoes.host}")
public interface NotificaAvisoViagem {

    @PostMapping(path = "/{id}/avisos", consumes = MediaType.APPLICATION_JSON_VALUE)
    void notificar(@PathVariable(name = "id") String numeroCartao,
                   @RequestBody NotificaAvisoViagemRequest notificaAvisoViagemRequest);

}
