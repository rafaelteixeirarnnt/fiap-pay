package br.com.fiap.fiappay.repositories;

import br.com.fiap.fiappay.models.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientesRepository extends JpaRepository<Clientes, UUID> {

    Optional<Clientes> findByCpf(String cpf);

}
