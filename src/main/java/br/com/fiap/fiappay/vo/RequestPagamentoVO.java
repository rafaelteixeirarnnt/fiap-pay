package br.com.fiap.fiappay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record RequestPagamentoVO(@Schema(description = "CPF do Cliente", example = "11111111111")
                                 @CPF(message = "CPF inválido")
                                 @NotNull(message = "CPF não pode ser nulo")
                                 @NotBlank(message = "CPF não pode ser vazio")
                                 @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
                                 String cpf,

                                 @NotNull(message = "Número do cartão não pode ser nulo")
                                 @NotBlank(message = "Número do cartão não pode ser vazio")
                                 @Pattern(regexp = "\\d{16}", message = "Número do cartão deve conter exatamente 16 dígitos")
                                 @Schema(description = "Número do Cartão", example = "************1234")
                                 String numero,

                                 @NotNull(message = "Data de validade não pode ser nula")
                                 @NotBlank(message = "Data de validade não pode ser vazia")
                                 @Pattern(regexp = "\\d{2}/\\d{2}", message = "Data de validade deve estar no formato MM/YY")
                                 String dataValidade,

                                 @NotNull(message = "CVV não pode ser nulo")
                                 @NotBlank(message = "CVV não pode ser vazio")
                                 @Pattern(regexp = "\\d{3}", message = "CVV deve conter exatamente 3 dígitos")
                                 String cvv,

                                 @NotNull(message = "Valor não pode ser nulo")
                                 @Positive(message = "Valor deve ser positivo")
                                 BigDecimal valor) {
}
