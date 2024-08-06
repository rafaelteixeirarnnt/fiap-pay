package br.com.fiap.fiappay.repositories;

import br.com.fiap.fiappay.models.CartoesClientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartoesClientesRepository extends JpaRepository<CartoesClientes, UUID> {
}
