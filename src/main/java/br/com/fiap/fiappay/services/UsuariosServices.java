package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.repositories.UsuariosRepository;
import br.com.fiap.fiappay.vo.RequestLoginVO;
import br.com.fiap.fiappay.vo.ResponseAutenticacaoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuariosServices {

    private final JwtService service;
    private final UsuariosRepository repositor;
    private final PasswordEncoder passwordEncoder;

    public ResponseAutenticacaoVO logar(RequestLoginVO vo) {
        var usuario = repositor.findByLogin(vo.usuario()).orElseThrow(() -> new NegocioException("Usuário não localizado"));

        if (this.passwordEncoder.matches(vo.senha(), usuario.getSenha())) {
            var tokenJwt = this.service.generateToken(usuario);
            return new ResponseAutenticacaoVO(tokenJwt);
        } else {
            throw new NegocioException("Credenciais incorretas. Por favor informe novamente!");
        }
    }

}
