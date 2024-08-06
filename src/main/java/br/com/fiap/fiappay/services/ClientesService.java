package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.mappers.ClientesMapper;
import br.com.fiap.fiappay.models.Clientes;
import br.com.fiap.fiappay.repositories.ClientesRepository;
import br.com.fiap.fiappay.vo.RequestClienteVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientesService {

    private final ClientesMapper mapper;
    private final ClientesRepository repository;

    public void salvar(RequestClienteVO vo) {

        var cliente = this.mapper.toEntity(vo);

        validarSeClienteJahCadastrado(cliente);
        removerCaracteresEspeciais(cliente);

        this.repository.save(cliente);

    }

    public Optional<Clientes> buscarPorCpf(String cpf) {
        return this.repository.findByCpf(cpf);
    }

    private synchronized void validarSeClienteJahCadastrado(Clientes cliente) {
        var clienteCadastrado = this.repository.findByCpf(cliente.getCpf());

        if (clienteCadastrado.isPresent()) {
            throw new NegocioException("Cliente já cadastrado");
        }
    }

    private void removerCaracteresEspeciais(Clientes cliente) {
        String telefoneSemMascara = removerEspacoECaracteresEspeciais(cliente.getTelefone());
        String cepSemMascara = removerEspacoECaracteresEspeciais(cliente.getCep());
        cliente.setTelefone(telefoneSemMascara);
        cliente.setCep(cepSemMascara);
    }

    private String removerEspacoECaracteresEspeciais(String campo) {
        return campo.replaceAll("[^0-9]", "").trim();
    }

}
