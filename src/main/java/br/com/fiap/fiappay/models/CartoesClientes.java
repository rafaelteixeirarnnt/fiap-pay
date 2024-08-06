package br.com.fiap.fiappay.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cartoes_clientes", schema = "fiappay")
public class CartoesClientes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false, updatable = false)
    private Clientes cliente;

    @ManyToOne
    @JoinColumn(name = "id_cartao", nullable = false, updatable = false)
    private Cartoes cartao;

    public CartoesClientes(Clientes cliente, Cartoes cartao) {
        this.cliente = cliente;
        this.cartao = cartao;
    }
}
