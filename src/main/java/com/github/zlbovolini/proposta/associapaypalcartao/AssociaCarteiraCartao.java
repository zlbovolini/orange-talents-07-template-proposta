package com.github.zlbovolini.proposta.associapaypalcartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "associa-carteira", url = "${cartoes.host}")
public interface AssociaCarteiraCartao {

    @PostMapping(path = "/{id}/carteiras", consumes = MediaType.APPLICATION_JSON_VALUE)
    void associar(@PathVariable(name = "id") String numeroCartao, @RequestBody AssociaCarteiraCartaoRequest request);
}
