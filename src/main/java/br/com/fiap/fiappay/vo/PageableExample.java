package br.com.fiap.fiappay.vo;

import io.swagger.v3.oas.annotations.media.Schema;

public record PageableExample(@Schema(description = "Número da página (começa em 0)", example = "0")
                              int page,
                              @Schema(description = "Tamanho da página", example = "10")
                              int size,
                              @Schema(description = "Ordenação no formato: propriedade.", example = "dataCompra")
                              String sort) {
}
