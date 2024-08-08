package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.mappers.CartoesMapper;
import br.com.fiap.fiappay.models.CartoesClientes;
import br.com.fiap.fiappay.models.Clientes;
import br.com.fiap.fiappay.repositories.CartoesClientesRepository;
import br.com.fiap.fiappay.repositories.CartoesRepository;
import br.com.fiap.fiappay.vo.RequestCartaoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public Cartoes obterCartaoPorNumeroDataValidadeCvvECliente(String numero, String dataValidade, String cvv, Clientes cliente) {
        var cartao = this.repository.findByNumeroAndDataValidadeAndCvv(numero, dataValidade, cvv)
                .orElseThrow(() -> new NegocioException("Cartão não localizado"));

        this.cartoesClientesRepository.findByClienteAndCartao(cliente, cartao)
                .orElseThrow(() -> new NegocioException("Cartão não pertence ao cliente"));

        return cartao;
    }

    @Transactional
    public synchronized void salvarNovoLimite(Cartoes cartao, BigDecimal valorCompra) {
        cartao.setLimite(cartao.getLimite().subtract(valorCompra));
        this.repository.save(cartao);

    }

    public void cartaoehValido(Cartoes cartao) {
        var cartaoDb = this.repository.findById(cartao.getId())
                .orElseThrow(() -> new NegocioException("Cartão não localizado"));

        var dataValidadeCartao = criarLocalDateComDataValidadeCartao(cartaoDb);
        var dataAtual = LocalDate.now().withDayOfMonth(1);

        if(dataValidadeCartao.isBefore(dataAtual)) {
            throw new NegocioException("Cartão vencido");
        }
    }

    private LocalDate criarLocalDateComDataValidadeCartao(Cartoes cartaoDb) {
        var dataValidade = cartaoDb.getDataValidade();
        var dataValidadeSplit = dataValidade.split("/");
        var mes = Integer.parseInt(dataValidadeSplit[0]);
        var ano = Integer.parseInt("20" + dataValidadeSplit[1]);
        return LocalDate.of(ano, mes, 1);
    }
}
