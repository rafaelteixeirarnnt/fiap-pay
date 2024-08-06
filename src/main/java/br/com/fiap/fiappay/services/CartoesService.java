package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.mappers.CartoesMapper;
import br.com.fiap.fiappay.models.Cartoes;
import br.com.fiap.fiappay.models.CartoesClientes;
import br.com.fiap.fiappay.repositories.CartoesClientesRepository;
import br.com.fiap.fiappay.repositories.CartoesRepository;
import br.com.fiap.fiappay.vo.RequestCartaoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartoesService {

    private final CartoesMapper mapper;
    private final CartoesRepository repository;
    private final ClientesService clientesService;
    private final CartoesClientesRepository cartoesClientesRepository;

    @Value("${fiappay.quantidade-cartoes}")
    private Integer quantidadeCartoes;

    public void salvar(RequestCartaoVO vo) {
        var cartao = this.mapper.toCartao(vo);

        this.clientesService.buscarPorCpf(vo.cpf())
                .ifPresentOrElse(cliente -> {

//                    quantidadeCartoes - Validar se o cliente já possui a quantidade máxima de cartões
                    var cartaoSalvo = this.repository.save(cartao);
                    var cartaoCliente = new CartoesClientes(cliente, cartaoSalvo);
                    this.cartoesClientesRepository.save(cartaoCliente);
                }, () -> {
                    throw new NegocioException("Cliente não encontrado");
                });
    }

}
