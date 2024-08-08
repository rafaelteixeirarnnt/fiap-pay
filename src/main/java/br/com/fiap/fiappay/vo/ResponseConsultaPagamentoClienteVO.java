package br.com.fiap.fiappay.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ResponseConsultaPagamentoClienteVO(BigDecimal valor,
                                                 String descricao,
                                                 @JsonProperty("metodo_pagamento")
                                                 String metodoPagamento,
                                                 String status) {
}
