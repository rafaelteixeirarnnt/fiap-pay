package br.com.fiap.fiappay.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestLoginVO(@NotNull
                             @NotBlank
                             @Schema(description = "Login do usuário", example = "adj2")
                             String usuario,

                             @NotNull
                             @NotBlank
                             @Schema(description = "Senha do usuário", example = "adj2@1234")
                             String senha) { }
