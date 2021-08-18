package com.github.zlbovolini.proposta.comum;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    List<Proposta> findByStatusAndNumeroCartaoIsNull(PropostaStatus status);
}
