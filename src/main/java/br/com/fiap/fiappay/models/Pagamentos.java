package br.com.fiap.fiappay.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_pagamentos", schema = "fiappay")
public class Pagamentos {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false, updatable = false)
    private Clientes cliente;

    @ManyToOne
    @JoinColumn(name = "id_cartao", nullable = false, updatable = false)
    private Cartoes cartao;

    @NotNull
    @Column(name = "vl_compra", nullable = false)
    private BigDecimal valorCompra;

    @NotNull
    @Column(name = "dthr_compra", nullable = false)
    private LocalDateTime dataCompra;

}
