package com.github.zlbovolini.proposta.comum;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartaoRepository extends CrudRepository<Cartao, Long> {

    Optional<Cartao> findByUuid(UUID uuid);

    Boolean existsByUuid(UUID uuid);

    Optional<Cartao> findByUuidAndUuidIsNotNull(UUID uuid);
}
