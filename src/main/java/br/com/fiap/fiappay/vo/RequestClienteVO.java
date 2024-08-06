package br.com.fiap.fiappay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record RequestClienteVO(@NotNull(message = "CPF não pode ser nulo")
                               @NotBlank(message = "CPF não pode ser vazio")
                               @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
                               @Schema(description = "CPF do Cliente", example = "11111111111")
                               @CPF(message = "CPF inválido")
                               String cpf,

                               @NotNull(message = "Nome não pode ser nulo")
                               @NotBlank(message = "Nome não pode ser vazio")
                               @Size(max = 100, message = "Nome não pode ter mais de 100 caracteres")
                               @Schema(description = "Nome do Cliente", example = "João da Silva")
                               String nome,

                               @NotNull(message = "Email não pode ser nulo")
                               @NotBlank(message = "Email não pode ser vazio")
                               @Email(message = "Email deve ser válido")
                               @Size(max = 50, message = "Email não pode ter mais de 50 caracteres")
                               @Schema(description = "E-mail do Cliente", example = "joao@example.com")
                               String email,

                               @NotNull(message = "Telefone não pode ser nulo")
                               @NotBlank(message = "Telefone não pode ser vazio")
                               @Size(max = 17, message = "Telefone não pode ter mais de 17 caracteres")
                               @Schema(description = "Telefone do Cliente", example = "+55 11 91234-5678")
                               @Pattern(regexp = "\\+\\d{2}\\s?\\d{2}\\s?\\d{4,5}-\\d{4}", message = "Telefone deve estar no formato +55 11 91234-5678")
                               String telefone,

                               @NotNull(message = "Rua não pode ser nula")
                               @NotBlank(message = "Rua não pode ser vazia")
                               @Size(max = 30, message = "Rua não pode ter mais de 30 caracteres")
                               @Schema(description = "Rua do Cliente", example = "Rua A")
                               String rua,

                               @NotNull(message = "Cidade não pode ser nula")
                               @NotBlank(message = "Cidade não pode ser vazia")
                               @Size(max = 20, message = "Cidade não pode ter mais de 20 caracteres")
                               @Schema(description = "Cidade do Cliente", example = "Cidade")
                               String cidade,

                               @NotNull(message = "Estado não pode ser nulo")
                               @NotBlank(message = "Estado não pode ser vazio")
                               @Size(max = 11, message = "Estado não pode ter mais de 11 caracteres")
                               @Schema(description = "Estado do Cliente", example = "Estado")
                               String estado,

                               @NotNull(message = "CEP não pode ser nulo")
                               @NotBlank(message = "CEP não pode ser vazio")
                               @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 12345-678")
                               @Size(max = 9, message = "CEP não pode ter mais de 9 caracteres")
                               @Schema(description = "CEP do Cliente", example = "12345-678")
                               String cep,

                               @NotNull(message = "País não pode ser nulo")
                               @NotBlank(message = "País não pode ser vazio")
                               @Size(max = 10, message = "País não pode ter mais de 10 caracteres")
                               @Schema(description = "País do Cliente", example = "Brasil")
                               String pais) {
}
