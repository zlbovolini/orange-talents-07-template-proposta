package com.github.zlbovolini.proposta.bloqueiacartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "bloqueio", url = "${cartoes.host}")
public interface SolicitaBloqueioCartao {

    @PostMapping(path = "/{id}/bloqueios", consumes = MediaType.APPLICATION_JSON_VALUE)
    void bloquear(@PathVariable(name = "id") String numeroCartao, @RequestBody Map<String, String> body);
}
