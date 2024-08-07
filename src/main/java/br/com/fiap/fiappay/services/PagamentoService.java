package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.controllers.exceptions.SemLimiteException;
import br.com.fiap.fiappay.models.Cartoes;
import br.com.fiap.fiappay.models.Pagamentos;
import br.com.fiap.fiappay.repositories.PagamentoRepository;
import br.com.fiap.fiappay.vo.RequestPagamentoVO;
import br.com.fiap.fiappay.vo.ResponsePagamentoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final ClientesService service;
    private final CartoesService cartoesService;
    private final PagamentoRepository repository;

    public ResponsePagamentoVO registrarPagamento(RequestPagamentoVO vo) {

        var cliente = this.service.buscarPorCpf(vo.cpf()).orElseThrow(() -> new NegocioException("Cliente não localizado"));
        var cartao = this.cartoesService.obterCartaoPorNumeroDataValidadeCvvECliente(vo.numero(), vo.dataValidade(), vo.cvv(), cliente);

        var pagamento = new Pagamentos(null, cliente, cartao, vo.valor());

        validarSeLimitesDeCartaoSaoSuficientes(pagamento, cartao);
        validarSeCartaoDentroDoPrazoDeValidade(cartao);

        this.cartoesService.salvarNovoLimite(cartao, pagamento.getValorCompra());

        return new ResponsePagamentoVO(this.repository.save(pagamento).getId());
    }

    private void validarSeCartaoDentroDoPrazoDeValidade(Cartoes cartao) {
        this.cartoesService.cartaoehValido(cartao);
    }

    private void validarSeLimitesDeCartaoSaoSuficientes(Pagamentos compra, Cartoes cartao) {
        if(cartao.getLimite().doubleValue() < compra.getValorCompra().doubleValue()) {
            throw new SemLimiteException("Limite do cartão insuficiente");
        }
    }
}
