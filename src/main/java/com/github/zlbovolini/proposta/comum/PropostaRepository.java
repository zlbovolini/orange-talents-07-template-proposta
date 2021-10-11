package com.github.zlbovolini.proposta.comum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    Optional<Proposta> findByUuid(UUID uuid);

    Page<Proposta> findByStatusAndCartaoIsNull(PropostaStatus status, Pageable pageable);

    Optional<String> findEmailByCartaoUuid(UUID cartaoUuid);

}
