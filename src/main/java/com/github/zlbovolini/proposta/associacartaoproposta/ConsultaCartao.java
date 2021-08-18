package com.github.zlbovolini.proposta.associacartaoproposta;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cartoes", url = "${cartoes.host}")
public interface ConsultaCartao {

    @GetMapping
    DadosCartaoResponse consulta(@RequestParam(name = "idProposta") Long propostaId);
}
