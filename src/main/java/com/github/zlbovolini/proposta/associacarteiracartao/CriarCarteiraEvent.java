package com.github.zlbovolini.proposta.associacarteiracartao;

import com.github.zlbovolini.proposta.criacarteira.Carteira;

public interface CriarCarteiraEvent {

    void executa(Carteira carteira);
}
