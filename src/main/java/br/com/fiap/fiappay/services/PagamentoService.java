package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.controllers.exceptions.SemLimiteException;
import br.com.fiap.fiappay.models.Cartoes;
import br.com.fiap.fiappay.models.Clientes;
import br.com.fiap.fiappay.models.Pagamentos;
import br.com.fiap.fiappay.repositories.PagamentoRepository;
import br.com.fiap.fiappay.vo.RequestPagamentoVO;
import br.com.fiap.fiappay.vo.ResponseConsultaPagamentoClienteVO;
import br.com.fiap.fiappay.vo.ResponsePagamentoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private static final String APROVADO = "aprovado";
    private static final String CARTAO_CREDITO = "cartao_credito";
    private static final String DESCRICAO_COMPRA = "Compra de produto X";

    private final CartoesService cartoesService;
    private final PagamentoRepository repository;
    private final ClientesService clientesService;

    @Transactional
    public ResponsePagamentoVO registrarPagamento(RequestPagamentoVO vo) {

        var cliente = this.clientesService.buscarPorCpf(vo.cpf()).orElseThrow(() -> new NegocioException("Cliente não localizado"));
        var cartao = this.cartoesService.obterCartaoPorNumeroDataValidadeCvvECliente(vo.numero(), vo.dataValidade(), vo.cvv(), cliente);

        var pagamento = new Pagamentos(null, cliente, cartao, vo.valor(), LocalDateTime.now());

        validarSeLimitesDeCartaoSaoSuficientes(pagamento, cartao);
        validarSeCartaoDentroDoPrazoDeValidade(cartao);
        this.cartoesService.salvarNovoLimite(cartao, pagamento.getValorCompra());

        return new ResponsePagamentoVO(this.repository.save(pagamento).getId());
    }

    private void validarSeCartaoDentroDoPrazoDeValidade(Cartoes cartao) {
        this.cartoesService.cartaoehValido(cartao);
    }

    private void validarSeLimitesDeCartaoSaoSuficientes(Pagamentos compra, Cartoes cartao) {
        if (cartao.getLimite().doubleValue() < compra.getValorCompra().doubleValue()) {
            throw new SemLimiteException("Limite do cartão insuficiente");
        }
    }

    public Page<ResponseConsultaPagamentoClienteVO> obterPagamentosPorCliente(String cpf, Pageable pageable) {
        Clientes cliente = this.clientesService.buscarPorCpf(cpf).orElseThrow(() -> new NegocioException("Cliente não localizado"));

        try {
            return this.repository.findByCliente(cliente, pageable)
                    .map(p -> new ResponseConsultaPagamentoClienteVO(
                            p.getValorCompra(),
                            DESCRICAO_COMPRA,
                            CARTAO_CREDITO,
                            APROVADO));
        } catch (PropertyReferenceException e) {
            throw new NegocioException("Parâmetro de ordenação inválido");
        }
    }
}
