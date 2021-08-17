package com.github.zlbovolini.proposta.consultadadossolicitante;

import com.github.zlbovolini.proposta.comum.DadosSolicitante;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "solicitacao", url = "${solicitacao.host}")
public interface ConsultaDadosFinanceiros {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    AnaliseDadosFinanceirosResponse consulta(DadosSolicitante dadosSolicitante);
}
