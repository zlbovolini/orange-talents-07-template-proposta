package com.github.zlbovolini.proposta.notificabloqueiocartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "bloqueio", url = "${cartoes.host}")
public interface NotificaBloqueioCartao {

    @PostMapping(path = "/{id}/bloqueios", consumes = MediaType.APPLICATION_JSON_VALUE)
    void notificar(@PathVariable(name = "id") String numeroCartao, @RequestBody Map<String, String> body);
}
