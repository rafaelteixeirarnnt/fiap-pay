package br.com.fiap.fiappay.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record RequestCartaoVO(@CPF(message = "CPF inválido")
                              @NotNull(message = "CPF não pode ser nulo")
                              @Schema(description = "CPF do Cliente", example = "12345678909")
                              @NotBlank(message = "CPF não pode ser vazio ou preenchido apenas com espaços")
                              @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
                              String cpf,

                              @NotNull(message = "Limite não pode ser nulo")
                              @Positive(message = "Limite deve ser um valor positivo")
                              @Schema(description = "Limite do Cartão", example = "1000")
                              BigDecimal limite,

                              @NotNull(message = "Número do cartão não pode ser nulo")
                              @NotBlank(message = "Número do cartão não pode ser vazio ou preenchido apenas com espaços")
                              @Pattern(regexp = "\\d{16}", message = "Número do cartão deve conter exatamente 16 dígitos")
                              @Schema(description = "Número do Cartão", example = "************1234")
                              String numero,

                              @JsonProperty("data_validade")
                              @NotNull(message = "Data de validade não pode ser nula")
                              @Schema(description = "Mês e Ano de validade do Cartão", example = "12/24")
                              @NotBlank(message = "Data de validade não pode ser vazia ou preenchida apenas com espaços")
                              @Pattern(regexp = "\\d{2}/\\d{2}", message = "Data de validade deve estar no formato MM/YY")
                              String dataValidade,

                              @NotNull(message = "CVV não pode ser nulo")
                              @Pattern(regexp = "\\d{3}", message = "CVV deve conter exatamente 3 dígitos")
                              @NotBlank(message = "CVV não pode ser vazio ou preenchido apenas com espaços")
                              @Schema(description = "CVV do Cartão", example = "123")
                              String cvv) {
}
