package com.github.zlbovolini.proposta.acompanhaproposta;

import com.github.zlbovolini.proposta.comum.PropostaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/propostas")
public class AcompanhaPropostaController {

    private final PropostaRepository propostaRepository;

    public AcompanhaPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @GetMapping("/{identificador}/status")
    public ResponseEntity<AcompanhaPropostaResponse> acompanha(@PathVariable UUID identificador) {
        return propostaRepository.findByUuid(identificador)
                .map(proposta -> {
                    AcompanhaPropostaResponse acompanhaPropostaResponse = new AcompanhaPropostaResponse(proposta);
                    return ResponseEntity.ok(acompanhaPropostaResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
