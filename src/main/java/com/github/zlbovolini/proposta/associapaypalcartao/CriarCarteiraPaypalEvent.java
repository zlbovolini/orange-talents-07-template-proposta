package com.github.zlbovolini.proposta.associapaypalcartao;

import com.github.zlbovolini.proposta.criacarteira.Carteira;

public interface CriarCarteiraPaypalEvent {

    void executa(Carteira carteira);
}
