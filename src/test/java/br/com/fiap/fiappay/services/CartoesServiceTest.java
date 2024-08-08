package br.com.fiap.fiappay.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.mappers.CartoesMapper;
import br.com.fiap.fiappay.models.Cartao;
import br.com.fiap.fiappay.models.Cliente;
import br.com.fiap.fiappay.repositories.CartoesRepository;
import br.com.fiap.fiappay.vo.RequestCartaoVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CartoesServiceTest {

    @Mock
    CartoesRepository cartoesRepository;

    @Mock
    ClientesService clientesService;

    private CartoesMapper mapper;

    RequestCartaoVO requestCartaoVO;

    @Captor
    ArgumentCaptor<Cartao> cartaoCaptor;

    @InjectMocks
    CartoesService cartoesService;

    @BeforeEach
    public void init() {
        mapper = Mappers.getMapper(CartoesMapper.class);
        ReflectionTestUtils.setField(cartoesService, "mapper", mapper);
        requestCartaoVO = new RequestCartaoVO(
            "CPF_01",
            new BigDecimal(1000),
            "1234567890123456",
            "12/24",
            "123"
        );
    }

    @Test
    void deveRegistrarPrimeiroCartaoParaCliente(){
        Cliente cliente = criarCliente("CPF_01");
        cartoesService = new CartoesService(mapper, cartoesRepository, clientesService);
        when(clientesService.buscarPorCpf(anyString())).thenReturn(Optional.of(cliente));
        when(cartoesRepository.findByNumero(anyString())).thenReturn(Optional.empty());
        when(cartoesRepository.save(cartaoCaptor.capture())).thenReturn(new Cartao());

        Cartao cartao = cartoesService.gerarNovoCartao(requestCartaoVO);

        assertNotNull(cartao);
        assertEquals(cliente, cartaoCaptor.getValue().getCliente());
        assertEquals("1234567890123456" ,cartaoCaptor.getValue().getNumero());
        assertEquals(1000, cartaoCaptor.getValue().getLimite().doubleValue());
        assertEquals(1000, cartaoCaptor.getValue().getSaldoDisponivel().doubleValue());
        assertEquals("12/24", cartaoCaptor.getValue().getDataValidade());
        assertEquals("123", cartaoCaptor.getValue().getCvv());
        verify(clientesService, times(1)).buscarPorCpf("CPF_01");
        verify(cartoesRepository, times(1)).findByNumero("1234567890123456");
        verify(cartoesRepository, times(1)).save(cartaoCaptor.getValue());
    }

    @Test
    void devePermitirRegistrarSegundoCartaoParaCliente(){
        Cartao primeiroCartao = new Cartao();
        Cliente cliente = new Cliente();
        cliente.setCartoes(List.of(primeiroCartao));
        cartoesService = new CartoesService(mapper, cartoesRepository, clientesService);
        when(clientesService.buscarPorCpf(anyString())).thenReturn(Optional.of(cliente));
        when(cartoesRepository.findByNumero(anyString())).thenReturn(Optional.empty());
        when(cartoesRepository.save(cartaoCaptor.capture())).thenReturn(new Cartao());

        Cartao cartao = cartoesService.gerarNovoCartao(requestCartaoVO);

        assertNotNull(cartao);
        assertEquals(cliente, cartaoCaptor.getValue().getCliente());
        assertEquals("1234567890123456" ,cartaoCaptor.getValue().getNumero());
        assertEquals(1000, cartaoCaptor.getValue().getLimite().doubleValue());
        assertEquals(1000, cartaoCaptor.getValue().getSaldoDisponivel().doubleValue());
        assertEquals("12/24", cartaoCaptor.getValue().getDataValidade());
        assertEquals("123", cartaoCaptor.getValue().getCvv());
        verify(clientesService, times(1)).buscarPorCpf("CPF_01");
        verify(cartoesRepository, times(1)).findByNumero("1234567890123456");
        verify(cartoesRepository, times(1)).save(cartaoCaptor.getValue());
    }

    @Test
    void naoDevePermitirRegistrarTerceiroCartaoParaCliente(){
        Cartao primeiroCartao = new Cartao();
        Cartao cartaoAdicional = new Cartao();
        Cliente cliente = new Cliente();
        cliente.setCartoes(List.of(primeiroCartao, cartaoAdicional));
        cartoesService = new CartoesService(mapper, cartoesRepository, clientesService);
        when(clientesService.buscarPorCpf(anyString())).thenReturn(Optional.of(cliente));
        when(cartoesRepository.findByNumero(anyString())).thenReturn(Optional.empty());

        try{
            cartoesService.gerarNovoCartao(requestCartaoVO);
            fail();

        } catch (NegocioException e) {
            assertEquals("Número máximo de cartões por cliente atingido", e.getMessage());
        }

        verify(clientesService, times(1)).buscarPorCpf("CPF_01");
        verify(cartoesRepository, times(1)).findByNumero("1234567890123456");
        verify(cartoesRepository, never()).save(any());
    }

    @Test
    void naoDevePermitirRegistrarCartaoQuandoClienteNaoCadastrado(){
        Cliente cliente = new Cliente();
        RequestCartaoVO requestCartaoVO = new RequestCartaoVO(
                "12345678901",
                new BigDecimal(1000),
                "1234567890123456",
                "12/24",
                "123"
        );
        cartoesService = new CartoesService(mapper, cartoesRepository, clientesService);
        when(clientesService.buscarPorCpf(anyString())).thenReturn(Optional.empty());

        try{
            cartoesService.gerarNovoCartao(requestCartaoVO);
            fail();

        } catch (NegocioException e) {
            assertEquals("Cliente não encontrado", e.getMessage());
        }

        verify(clientesService, times(1)).buscarPorCpf("12345678901");
        verify(cartoesRepository, never()).findByNumero(any());
        verify(cartoesRepository, never()).save(any());
    }

    @Test
    void naoDevePermitirRegistrarMesmoCartaoParaCliente(){
        Cliente clienteSolicitante = criarCliente("12345678901");
        Cliente donoCartao = criarCliente("12345678901");
        Cartao cartao = criarCartao("1234567890123456", donoCartao);
        cartoesService = new CartoesService(mapper, cartoesRepository, clientesService);
        when(clientesService.buscarPorCpf(anyString())).thenReturn(Optional.of(clienteSolicitante));
        when(cartoesRepository.findByNumero(anyString())).thenReturn(Optional.of(cartao));

        try{
            cartoesService.gerarNovoCartao(requestCartaoVO);
            fail();

        } catch (NegocioException e) {
            assertEquals("Cartão já registrado para esse cliente", e.getMessage());
        }

        verify(clientesService, times(1)).buscarPorCpf("CPF_01");
        verify(cartoesRepository, times(1)).findByNumero("1234567890123456");
        verify(cartoesRepository, never()).save(any());
    }

    @Test
    void naoDevePermitirRegistrarCartaoQuandoNumeroDoCartaoEstiverRegistradoParaOutroCliente(){
        Cliente clienteSolicitante = criarCliente("CPF_01");
        Cartao cartao = criarCartao("1234567890123456", criarCliente("CPF_02"));
        RequestCartaoVO requestCartaoVO = new RequestCartaoVO(
                "CPF_01",
                new BigDecimal(1000),
                "1234567890123456",
                "12/24",
                "123"
        );
        cartoesService = new CartoesService(mapper, cartoesRepository, clientesService);
        when(clientesService.buscarPorCpf(anyString())).thenReturn(Optional.of(clienteSolicitante));
        when(cartoesRepository.findByNumero(anyString())).thenReturn(Optional.of(cartao));

        try{
            cartoesService.gerarNovoCartao(requestCartaoVO);
            fail();

        } catch (NegocioException e) {
            assertEquals("Cartão já registrado para outro cliente", e.getMessage());
        }

        verify(clientesService, times(1)).buscarPorCpf("CPF_01");
        verify(cartoesRepository, times(1)).findByNumero("1234567890123456");
        verify(cartoesRepository, never()).save(any());
    }

    private Cliente criarCliente(String cpf){
        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);
        cliente.setCartoes(new ArrayList<>());
        return cliente;
    }

    private Cartao criarCartao(String numero, Cliente cliente){
        Cartao cartao = new Cartao();
        cartao.setNumero(numero);
        cartao.setCliente(cliente);
        return cartao;
    }

}
