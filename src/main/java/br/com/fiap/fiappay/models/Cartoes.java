package br.com.fiap.fiappay.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_cartoes", schema = "fiappay")
public class Cartoes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "vl_limite", nullable = false)
    private Double limite;

    @Column(name = "tx_numero", nullable = false)
    private String numero;

    @Column(name = "tx_data_validade", nullable = false)
    private String dataValidade;

    @Column(name = "tx_cvv", nullable = false)
    private String cvv;

}
