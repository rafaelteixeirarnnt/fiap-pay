package br.com.fiap.fiappay.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        Throwable cause = authException.getCause();
        if (cause instanceof TokenExpiredException) {
            response.sendError(SC_UNAUTHORIZED, "Token expirado");
        } else {
            response.sendError(SC_UNAUTHORIZED, "Usuário sem permissão");
        }
    }
}
