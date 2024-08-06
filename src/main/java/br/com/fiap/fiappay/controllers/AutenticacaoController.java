package br.com.fiap.fiappay.controllers;

import br.com.fiap.fiappay.services.UsuariosServices;
import br.com.fiap.fiappay.vo.RequestLoginVO;
import br.com.fiap.fiappay.vo.ResponseAutenticacaoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "1 - Login", description = "A API de Autenticação de Usuários permite que os usuários façam login no sistema. " +
        "Ela assegura a segurança e integridade dos dados, oferecendo um controle de acesso robusto baseado em permissões. " +
        "Os usuários podem autenticar suas credenciais para obter um token de acesso, que é necessário para acessar recursos protegidos.")
public class AutenticacaoController {


    private final UsuariosServices service;

    @PostMapping("/autenticacao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"token\": \"XXXXXXXXX\" }"),
                            schema = @Schema(implementation = ResponseAutenticacaoVO.class))),
            @ApiResponse(responseCode = "401", description = "Erro de autorização"),
            @ApiResponse(responseCode = "500", description = "Erro de negócio")
    })
    @Operation(summary = "Autenticação de Usuário", description = """
            Este endpoint permite a autenticação de usuários fornecendo um nome de usuário e senha.
            Se as credenciais forem válidas, um token de acesso será retornado, permitindo o acesso seguro a outros recursos da API.
            """)
    public ResponseEntity<ResponseAutenticacaoVO> autenticar(@RequestBody @Validated RequestLoginVO request) {
        return ResponseEntity.ok(this.service.logar(request));
    }

}
