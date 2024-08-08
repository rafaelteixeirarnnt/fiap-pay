package br.com.fiap.fiappay.repositories;

import br.com.fiap.fiappay.models.Cartao;
import br.com.fiap.fiappay.models.CartoesClientes;
import br.com.fiap.fiappay.models.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartoesClientesRepository extends JpaRepository<CartoesClientes, UUID> {

    Optional<CartoesClientes> findByClienteAndCartao(Cliente cliente, Cartao cartao);

    Page<CartoesClientes> findByCliente(Cliente cliente, Pageable pageable);
}
