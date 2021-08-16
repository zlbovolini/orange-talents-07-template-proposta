package com.github.zlbovolini.proposta.criaproposta;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class CriarPropostaController {

    private final PropostaRepository propostaRepository;

    public CriarPropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping
    public ResponseEntity<Void> criar(@Valid @RequestBody CriarPropostaRequest criarPropostaRequest) {
        Proposta proposta = criarPropostaRequest.toModel();

        propostaRepository.save(proposta);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(proposta.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
