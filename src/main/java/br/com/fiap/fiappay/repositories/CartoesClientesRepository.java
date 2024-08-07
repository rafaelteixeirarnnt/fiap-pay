package br.com.fiap.fiappay.repositories;

import br.com.fiap.fiappay.models.Cartoes;
import br.com.fiap.fiappay.models.CartoesClientes;
import br.com.fiap.fiappay.models.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartoesClientesRepository extends JpaRepository<CartoesClientes, UUID> {

    Optional<CartoesClientes> findByClienteAndCartao(Clientes cliente, Cartoes cartao);

}
