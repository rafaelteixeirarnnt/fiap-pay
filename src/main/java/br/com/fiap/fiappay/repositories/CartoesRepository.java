package br.com.fiap.fiappay.repositories;

import br.com.fiap.fiappay.models.Cartoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartoesRepository extends JpaRepository<Cartoes, UUID> {
}
