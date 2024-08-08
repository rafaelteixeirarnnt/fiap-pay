package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.models.Cartao;
import br.com.fiap.fiappay.models.Cliente;
import br.com.fiap.fiappay.models.Pagamentos;
import br.com.fiap.fiappay.repositories.PagamentoRepository;
import br.com.fiap.fiappay.vo.RequestPagamentoVO;
import br.com.fiap.fiappay.vo.ResponsePagamentoVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService pagamentoService;

    @Mock
    private CartoesService cartoesServiceMock;
    @Mock
    private PagamentoRepository pagamentoRepositoryMock;
    @Mock
    private ClientesService clientesServiceMock;

    @Captor
    ArgumentCaptor<Pagamentos> pagamentoCaptor;

    private RequestPagamentoVO requestPagamentoVO;

    private Cartao cartaoSaldo1000;

    private Cliente cliente_01;

    @BeforeEach
    public void init() {
        pagamentoService = new PagamentoService(cartoesServiceMock, pagamentoRepositoryMock, clientesServiceMock);
        cartaoSaldo1000 = criarCartao(new BigDecimal(1000));
        cliente_01 = criarCliente("01");

    }

    @Test
    void deveRegistrarNovoPagamentoEAtualizarSaldoDoCartao() {
        this.cliente_01.getCartoes().add(cartaoSaldo1000);
        RequestPagamentoVO vo = criarRequestPagamentoVO("cartao_01", new BigDecimal(100));
        when(clientesServiceMock.buscarPorCpf(anyString())).thenReturn(Optional.of(cliente_01));
        when(pagamentoRepositoryMock.save(pagamentoCaptor.capture())).thenReturn(new Pagamentos());

        ResponsePagamentoVO responsePagamentoVO = pagamentoService.registrarPagamento(vo);

        assertNotNull(responsePagamentoVO);
        assertEquals(new BigDecimal(900), cartaoSaldo1000.getSaldoDisponivel());
        assertEquals(cliente_01, pagamentoCaptor.getValue().getCliente());
        assertEquals(cartaoSaldo1000, pagamentoCaptor.getValue().getCartao());
        assertEquals(vo.valor(), pagamentoCaptor.getValue().getValorCompra());
        verify(clientesServiceMock, times(1)).buscarPorCpf(vo.cpf());
        verify(cartoesServiceMock, times(1)).validarDataValidade(cartaoSaldo1000);
        verify(cartoesServiceMock, times(1)).salvar(cartaoSaldo1000);
        verify(pagamentoRepositoryMock, times(1)).save(pagamentoCaptor.getValue());
    }

    @Test
    void naoDeveRegistrarPagamentoQuandoInformarCPFClienteInvalido() {
        RequestPagamentoVO vo = criarRequestPagamentoVO("cartao_01", new BigDecimal(100));
        when(clientesServiceMock.buscarPorCpf(anyString())).thenReturn(Optional.empty());

        try{
            pagamentoService.registrarPagamento(vo);
            fail();
        } catch (NegocioException e){
            assertEquals("Cliente não localizado", e.getMessage());
        }

        verify(clientesServiceMock, times(1)).buscarPorCpf(vo.cpf());
        verify(cartoesServiceMock, never()).validarDataValidade(any());
        verify(cartoesServiceMock, never()).salvar(any());
        verify(pagamentoRepositoryMock, never()).save(any());
    }

    @Test
    void naoDeveRegistrarPagamentoQuandoInformarNumeroDoCartaoIncorreto() {
        this.cliente_01.getCartoes().add(cartaoSaldo1000);
        RequestPagamentoVO vo = criarRequestPagamentoVO("numero_cartao_incorreto", new BigDecimal(100));
        when(clientesServiceMock.buscarPorCpf(anyString())).thenReturn(Optional.of(cliente_01));

        try{
            pagamentoService.registrarPagamento(vo);
            fail();
        } catch (NegocioException e){
            assertEquals("Dados do cartão inválidos", e.getMessage());
        }

        verify(clientesServiceMock, times(1)).buscarPorCpf(vo.cpf());
        verify(cartoesServiceMock, never()).validarDataValidade(any());
        verify(cartoesServiceMock, never()).salvar(any());
        verify(pagamentoRepositoryMock, never()).save(any());
    }

    @Test
    void naoDeveRegistrarPagamentoQuandoCartaoInformadoEstiverComDataVencida() {
        this.cliente_01.getCartoes().add(cartaoSaldo1000);
        RequestPagamentoVO vo = criarRequestPagamentoVO("cartao_01", new BigDecimal(1000));
        when(clientesServiceMock.buscarPorCpf(anyString())).thenReturn(Optional.of(cliente_01));
        doThrow(new NegocioException("Cartão vencido")).when(cartoesServiceMock).validarDataValidade(any());

        try{
            pagamentoService.registrarPagamento(vo);
            fail();
        } catch (NegocioException e){
            assertEquals("Cartão vencido", e.getMessage());
        }

        verify(clientesServiceMock, times(1)).buscarPorCpf(vo.cpf());
        verify(cartoesServiceMock, times(1)).validarDataValidade(cartaoSaldo1000);
        verify(cartoesServiceMock, never()).salvar(any());
        verify(pagamentoRepositoryMock, never()).save(any());
    }

    @Test
    void naoDeveRegistrarPagamentoQuandoSaldoDoCartaoForMenorQueValorDaCompra() {
        this.cliente_01.getCartoes().add(cartaoSaldo1000);
        BigDecimal valorCompra = new BigDecimal(1001);
        RequestPagamentoVO vo = criarRequestPagamentoVO("cartao_01", valorCompra);
        when(clientesServiceMock.buscarPorCpf(anyString())).thenReturn(Optional.of(cliente_01));

        try{
            pagamentoService.registrarPagamento(vo);
            fail();
        } catch (NegocioException e){
            assertEquals("Saldo insuficiente", e.getMessage());
        }

        verify(clientesServiceMock, times(1)).buscarPorCpf(vo.cpf());
        verify(cartoesServiceMock, times(1)).validarDataValidade(cartaoSaldo1000);
        verify(cartoesServiceMock, never()).salvar(any());
        verify(pagamentoRepositoryMock, never()).save(any());
    }

    private RequestPagamentoVO criarRequestPagamentoVO(String numeroCartao, BigDecimal valorCompra) {
        return new RequestPagamentoVO(
                "11111111111",
                numeroCartao,
                "01/2024",
                "123",
                valorCompra
        );
    }

    private Cartao criarCartao(BigDecimal saldoDisponivel) {
        Cartao cartao = new Cartao();
        cartao.setNumero("cartao_01");
        cartao.setDataValidade("01/2024");
        cartao.setCvv("123");
        cartao.setSaldoDisponivel(saldoDisponivel);
        return cartao;
    }

    private Cliente criarCliente(String cpf){
        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);
        cliente.setCartoes(new ArrayList<>());
        return cliente;
    }
}
