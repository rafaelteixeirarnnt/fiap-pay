package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.mappers.CartoesMapper;
import br.com.fiap.fiappay.models.Cartao;
import br.com.fiap.fiappay.models.Cliente;
import br.com.fiap.fiappay.repositories.CartoesRepository;
import br.com.fiap.fiappay.vo.RequestCartaoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartoesService {

    private final CartoesMapper mapper;
    private final CartoesRepository repository;
    private final ClientesService clientesService;
    private final static int TOTAL_CARTAO_POR_CLIENTE = 2;


    public Cartao gerarNovoCartao(RequestCartaoVO requestCartaoVO) {
        Cliente cliente = clientesService.buscarPorCpf(requestCartaoVO.cpf())
                .orElseThrow(() -> new NegocioException("Cliente não encontrado"));
        validarCartao(requestCartaoVO, cliente);
        List<Cartao> cartoesCliente = cliente.getCartoes();
        if(cartoesCliente.size() >= TOTAL_CARTAO_POR_CLIENTE){
            throw new NegocioException("Número máximo de cartões por cliente atingido");
        }

        Cartao cartao = criarCartao(requestCartaoVO, cliente);
        return repository.save(cartao);
    }

    public Cartao salvar(Cartao cartao) {
        return repository.save(cartao);
    }

    private Cartao criarCartao(RequestCartaoVO requestCartaoVO, Cliente cliente) {
        Cartao cartao = this.mapper.toCartao(requestCartaoVO);
        cartao.setSaldoDisponivel(requestCartaoVO.limite());
        cartao.setCliente(cliente);
        return cartao;
    }

    private void validarCartao(RequestCartaoVO requestCartaoVO, Cliente cliente) {
        Optional<Cartao> cartao = buscarCartao(requestCartaoVO.numero());
        if(cartao.isPresent()) {
            Cliente donoCartao = cartao.get().getCliente();
            String msgRegra = donoCartao.getCpf().equals(cliente.getCpf()) ?
                    "Cartão já registrado para esse cliente" :
                    "Cartão já registrado para outro cliente";

            throw new NegocioException(msgRegra);
        }
    }

    public Optional<Cartao> buscarCartao(String numero) {
        return repository.findByNumero(numero);
    }

    public void validarDataValidade(Cartao cartao) {
        var dataValidadeCartao = criarLocalDateComDataValidadeCartao(cartao.getDataValidade());
        var dataAtual = LocalDate.now().withDayOfMonth(1);

        if(dataValidadeCartao.isBefore(dataAtual)) {
            throw new NegocioException("Cartão vencido");
        }
    }

    private LocalDate criarLocalDateComDataValidadeCartao(String dataValidadeCartao) {
        var dataValidadeSplit = dataValidadeCartao.split("/");
        var mes = Integer.parseInt(dataValidadeSplit[0]);
        var ano = Integer.parseInt("20" + dataValidadeSplit[1]);
        return LocalDate.of(ano, mes, 1);
    }

}
