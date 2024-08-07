package br.com.fiap.fiappay.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ResponsePagamentoVO(
        @JsonProperty("chave_pagamento")
        UUID chavePagamento) {
}
