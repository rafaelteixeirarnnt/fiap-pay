package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.models.Cartao;
import br.com.fiap.fiappay.models.Cliente;
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

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        var cartao = localizarCartao(vo, cliente.getCartoes()).orElseThrow(() -> new NegocioException("Dados do cartão inválidos"));
        BigDecimal saldoDisponivel = cartao.getSaldoDisponivel();
        BigDecimal valorSolicitado = vo.valor();

        this.cartoesService.validarDataValidade(cartao);
        if(saldoDisponivel.compareTo(valorSolicitado) < 0){
            throw new NegocioException("Saldo insuficiente");
        }

        BigDecimal novoSaldo = saldoDisponivel.subtract(valorSolicitado);
        cartao.setSaldoDisponivel(novoSaldo);
        cartoesService.salvar(cartao);
        var pagamento = new Pagamentos(null, cliente, cartao, valorSolicitado, LocalDateTime.now());
        return new ResponsePagamentoVO(this.repository.save(pagamento).getId());
    }

    private Optional<Cartao> localizarCartao(RequestPagamentoVO vo, List<Cartao> cartoes) {
        return cartoes.stream().filter(c ->
                c.getNumero().equals(vo.numero()) && c.getCvv().equals(vo.cvv()) && c.getDataValidade().equals(vo.dataValidade())
        ).findFirst();
    }

    public Page<ResponseConsultaPagamentoClienteVO> obterPagamentosPorCliente(String cpf, Pageable pageable) {
        Cliente cliente = this.clientesService.buscarPorCpf(cpf).orElseThrow(() -> new NegocioException("Cliente não localizado"));

        try {
            return this.repository.findByCliente(cliente, pageable)
                    .map(p -> new ResponseConsultaPagamentoClienteVO(
                            p.getValorCompra(),
                            DESCRICAO_COMPRA,
                            CARTAO_CREDITO,
                            APROVADO));
        } catch (PropertyReferenceException e) {
            System.out.println(e);
            throw new NegocioException("Parâmetro de ordenação inválido");
        }
    }
}
