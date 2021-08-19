package com.github.zlbovolini.proposta.comum;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartaoRepository extends CrudRepository<Cartao, Long> {

    Optional<Cartao> findByNumeroAndNumeroIsNotNull(String numero);
}
