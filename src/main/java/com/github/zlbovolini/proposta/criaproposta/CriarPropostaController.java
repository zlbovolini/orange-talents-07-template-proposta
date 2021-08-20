package com.github.zlbovolini.proposta.criaproposta;

import com.github.zlbovolini.proposta.comum.DadosSolicitante;
import com.github.zlbovolini.proposta.comum.Proposta;
import com.github.zlbovolini.proposta.comum.PropostaRepository;
import com.github.zlbovolini.proposta.consultadadossolicitante.AnaliseDadosFinanceirosService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/propostas")
public class CriarPropostaController {

    private final PropostaRepository propostaRepository;
    private final AnaliseDadosFinanceirosService analiseDadosFinanceirosService;
    private final TransactionTemplate transactionTemplate;

    public CriarPropostaController(PropostaRepository propostaRepository, AnaliseDadosFinanceirosService analiseDadosFinanceirosService,
                                   TransactionTemplate transactionTemplate) {
        this.propostaRepository = propostaRepository;
        this.analiseDadosFinanceirosService = analiseDadosFinanceirosService;
        this.transactionTemplate = transactionTemplate;
    }

    @PostMapping
    public ResponseEntity<Void> criar(@Valid @RequestBody CriarPropostaRequest criarPropostaRequest) {
        Proposta proposta = criarPropostaRequest.toModel();

        transactionTemplate.execute(status -> propostaRepository.save(proposta));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(proposta.getUuid())
                .toUri();

        DadosSolicitante dadosSolicitante = new DadosSolicitante(proposta);
        analiseDadosFinanceirosService.execute(dadosSolicitante);

        return ResponseEntity.created(location).build();
    }
}
