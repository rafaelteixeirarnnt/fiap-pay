package br.com.fiap.fiappay.controllers;

import br.com.fiap.fiappay.services.PagamentoService;
import br.com.fiap.fiappay.vo.RequestPagamentoVO;
import br.com.fiap.fiappay.vo.ResponsePagamentoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.fiap.fiappay.utils.PathUtils.PATH_API;

@RestController
@RequiredArgsConstructor
@RequestMapping(PATH_API)
@Tag(name = "4 - Pagamento", description = "API para registro de pagamentos")
public class PagamentoController {

    private final PagamentoService service;

    @PostMapping("/pagamentos")
    @Operation(summary = "Autorizar um pagamento",
            description = "Autorizar um pagamento com cartão de crédito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"chave_pagamento\": \"XXXXXXXXX\" }"),
                            schema = @Schema(implementation = ResponsePagamentoVO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro de negócio")
    })
    public ResponseEntity<ResponsePagamentoVO> autorizarPagamento(@Valid @RequestBody RequestPagamentoVO vo) {
        return ResponseEntity.ok(service.registrarPagamento(vo));
    }

}
