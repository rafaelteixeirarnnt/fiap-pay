package br.com.fiap.fiappay.repositories;

import br.com.fiap.fiappay.models.Clientes;
import br.com.fiap.fiappay.models.Pagamentos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamentos, UUID> {

    Page<Pagamentos> findByCliente(Clientes cliente, Pageable pageable);
}
