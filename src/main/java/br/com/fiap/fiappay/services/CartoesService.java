package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.mappers.CartoesMapper;
import br.com.fiap.fiappay.models.Cartoes;
import br.com.fiap.fiappay.repositories.CartoesRepository;
import br.com.fiap.fiappay.vo.RequestCartaoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartoesService {

    private final CartoesMapper mapper;
    private final CartoesRepository repository;

    public void salvar(RequestCartaoVO vo) {
        Cartoes cartao = this.mapper.toCartao(vo);
        System.out.println(cartao);
    }

}
