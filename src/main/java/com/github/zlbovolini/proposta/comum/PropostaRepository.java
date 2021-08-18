package com.github.zlbovolini.proposta.comum;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    Optional<Proposta> findByIdentificador(UUID identificado);

    List<Proposta> findByStatusAndNumeroCartaoIsNull(PropostaStatus status);
}
