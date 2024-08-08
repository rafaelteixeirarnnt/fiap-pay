package br.com.fiap.fiappay.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cartoes", schema = "fiappay")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "vl_limite", nullable = false, precision = 12, scale = 2)
    private BigDecimal limite;

    @Column(name = "tx_numero", nullable = false, length = 16)
    private String numero;

    @Column(name = "tx_data_validade", nullable = false, length = 5)
    private String dataValidade;

    @Column(name = "tx_cvv", nullable = false, length = 3)
    private String cvv;

    @Column(name = "vl_saldo", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoDisponivel;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

}
