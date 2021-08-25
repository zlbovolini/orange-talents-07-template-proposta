package com.github.zlbovolini.proposta.associapaypalcartao;

import com.github.zlbovolini.proposta.criacarteira.Carteira;
import com.github.zlbovolini.proposta.criacarteira.CarteiraRepository;
import com.github.zlbovolini.proposta.exception.ApiErrorException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

@Service
public class AssociaCarteiraPaypalService implements CriarCarteiraPaypalEvent {

    private final AssociaCarteiraCartao associaCarteiraCartao;
    private final CarteiraRepository carteiraRepository;
    private final TransactionTemplate transactionTemplate;

    private final String erro = "Não foi possível associar a carteira ao cartão";

    public AssociaCarteiraPaypalService(AssociaCarteiraCartao associaCarteiraCartao,
                                        CarteiraRepository carteiraRepository,
                                        TransactionTemplate transactionTemplate) {
        this.associaCarteiraCartao = associaCarteiraCartao;
        this.carteiraRepository = carteiraRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void executa(Carteira carteira) {

        String numero = carteira.getCartao().getNumero();
        AssociaCarteiraCartaoRequest request = new AssociaCarteiraCartaoRequest(carteira);

        try {
            associaCarteiraCartao.associar(numero, request);
            // pode gerar duplicadas
            transactionTemplate.execute(status -> carteiraRepository.save(carteira));
        } catch (FeignException.FeignClientException fce) {
            HttpStatus status = Optional.ofNullable(HttpStatus.resolve(fce.status()))
                    .orElse(HttpStatus.BAD_REQUEST);
            throw new ApiErrorException(status, erro);
        } catch (FeignException.FeignServerException fse) {
            HttpStatus status = Optional.ofNullable(HttpStatus.resolve(fse.status()))
                    .orElse(HttpStatus.SERVICE_UNAVAILABLE);
            throw new ApiErrorException(status, erro);
        } catch (FeignException fe) {
            HttpStatus status = Optional.ofNullable(HttpStatus.resolve(fe.status()))
                    .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
            if (!status.is2xxSuccessful()) {
                throw new ApiErrorException(status, erro);
            }
        }
    }
}
