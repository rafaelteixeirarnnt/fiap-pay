package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.controllers.exceptions.NegocioException;
import br.com.fiap.fiappay.repositories.UsuariosRepository;
import br.com.fiap.fiappay.vo.RequestLoginVO;
import br.com.fiap.fiappay.vo.ResponseAutenticacaoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuariosServices {

    private final UsuariosRepository repositor;

    public ResponseAutenticacaoVO logar(RequestLoginVO vo) {
        var usuario = repositor.findByLogin(vo.usuario()).orElseThrow(() -> new NegocioException("Usuário não localizado"));
        return null;
    }

}
