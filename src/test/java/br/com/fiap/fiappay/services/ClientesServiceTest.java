package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.mappers.ClientesMapper;
import br.com.fiap.fiappay.models.Cliente;
import br.com.fiap.fiappay.repositories.ClientesRepository;
import br.com.fiap.fiappay.vo.RequestClienteVO;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientesServiceTest {

    @Mock
    private ClientesRepository clientesRepository;

    ClientesMapper clientesMapper;

    @InjectMocks
    private ClientesService clientesService;

    @Captor
    ArgumentCaptor<Cliente> clienteCaptor;

    @BeforeEach
    public void init() {
        clientesMapper = Mappers.getMapper(ClientesMapper.class);
        ReflectionTestUtils.setField(clientesService, "mapper", clientesMapper);
    }

    @Test
    void deveTratarCamposERegistrarUmNovoClienteComSucesso() {
        clientesService = new ClientesService(clientesMapper, clientesRepository);
        RequestClienteVO requestClienteVO = new RequestClienteVO(
                "1111111111",
                "João da Silva",
                "joao@example.com",
                "+55 11 91234-45678",
                "Rua A",
                "Cidade",
                "Estado",
                "12345-678",
                "Brasil"
        );
        when(clientesRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(clientesRepository.save(clienteCaptor.capture())).thenReturn(new Cliente());

        Cliente novoCliente = clientesService.registrar(requestClienteVO);

        assertNotNull(novoCliente);
        verify(clientesRepository, times(1)).findByCpf("1111111111");
        assertNull(clienteCaptor.getValue().getId());
        assertEquals("1111111111", clienteCaptor.getValue().getCpf());
        assertEquals("João da Silva", clienteCaptor.getValue().getNome());
        assertEquals("joao@example.com", clienteCaptor.getValue().getEmail());
        assertEquals("55119123445678", clienteCaptor.getValue().getTelefone());
        assertEquals("Rua A", clienteCaptor.getValue().getRua());
        assertEquals("Cidade", clienteCaptor.getValue().getCidade());
        assertEquals("Estado", clienteCaptor.getValue().getEstado());
        assertEquals("12345678", clienteCaptor.getValue().getCep());
        assertEquals("Brasil", clienteCaptor.getValue().getPais());
    }

    @Test
    void deveLancarUmaExcepcaoAoTentarRegistrarClienteJaCadastrado() {
        clientesService = new ClientesService(clientesMapper, clientesRepository);
        RequestClienteVO requestClienteVO = new RequestClienteVO(
                "1111111111",
                "João da Silva",
                "joao@example.com",
                "+55 11 91234-45678",
                "Rua A",
                "Cidade",
                "Estado",
                "12345-678",
                "Brasil"
        );
        when(clientesRepository.findByCpf("1111111111")).thenReturn(Optional.of(new Cliente()));

        try {
            clientesService.registrar(requestClienteVO);
            fail();
        } catch (NegocioException e) {
            assertEquals("Cliente já registrado", e.getMessage());
        }
        verify(clientesRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveBuscarClientePorCpf() {
        clientesService = new ClientesService(clientesMapper, clientesRepository);
        when(clientesRepository.findByCpf(anyString())).thenReturn(Optional.of(new Cliente()));

        Optional<Cliente> cliente = clientesService.buscarPorCpf("1111111111");

        assertTrue(cliente.isPresent());
        verify(clientesRepository, times(1)).findByCpf("1111111111");
    }

}
