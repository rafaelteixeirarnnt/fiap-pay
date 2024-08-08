package br.com.fiap.fiappay.controllers;

import br.com.fiap.fiappay.services.CartoesService;
import br.com.fiap.fiappay.vo.RequestCartaoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.fiap.fiappay.utils.PathUtils.PATH_API;

@RestController
@RequiredArgsConstructor
@RequestMapping(PATH_API)
@Tag(name = "3 - Cartão", description = "API para gerenciamento de cartões dos clientes.")
public class CartoesController {


    private final CartoesService service;

    @PostMapping("/cartao")
    @Operation(summary = "Gera um novo cartão para um cliente",
            description = """
                            Gera um novo cartão para um cliente. Cada cliente pode ter no máximo 2 cartões. 
                            Se o cliente já possuir 2 cartões, uma exceção será lançada.
                          """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "401", description = "Erro de autorização"),
            @ApiResponse(responseCode = "403", description = "Número máximo de cartões atingido"),
            @ApiResponse(responseCode = "500", description = "Erro de negócio")
    })
    public ResponseEntity gerarCartao(@RequestBody @Validated RequestCartaoVO vo) {
        this.service.gerarNovoCartao(vo);
        return ResponseEntity.ok().build();
    }

}
