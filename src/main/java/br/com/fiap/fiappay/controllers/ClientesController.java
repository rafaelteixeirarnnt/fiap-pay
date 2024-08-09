package br.com.fiap.fiappay.controllers;

import br.com.fiap.fiappay.models.Cliente;
import br.com.fiap.fiappay.services.ClientesService;
import br.com.fiap.fiappay.vo.RequestClienteVO;
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

import java.util.HashMap;
import java.util.Map;

import static br.com.fiap.fiappay.utils.PathUtils.PATH_API;

@RestController
@RequiredArgsConstructor
@RequestMapping(PATH_API)
@Tag(name = "2 - Cliente", description = "API para registro de clientes")
public class ClientesController {

    private final ClientesService service;

    @PostMapping("/cliente")
    @Operation(summary = "Registrar um novo cliente", description = "Registrar um novo cliente com os dados básicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "401", description = "Erro de autorização"),
            @ApiResponse(responseCode = "500", description = "Erro de negócio")
    })

    public ResponseEntity<?> salvar(@RequestBody @Validated RequestClienteVO vo) {
        Cliente cliente = this.service.registrar(vo);
        Map<String, String> idClienteMap = new HashMap<>();
        idClienteMap.put("id_cliente", cliente.getId().toString());
        return ResponseEntity.ok().body(idClienteMap);
    }

}
