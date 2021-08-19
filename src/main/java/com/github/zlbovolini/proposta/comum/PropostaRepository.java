package com.github.zlbovolini.proposta.comum;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    Optional<Proposta> findByUuid(UUID uuid);

    List<Proposta> findByStatusAndCartaoIsNull(PropostaStatus status);
}
