package br.com.fiap.fiappay.services;

import br.com.fiap.fiappay.models.Usuarios;
import br.com.fiap.fiappay.security.DateTimeProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    private final UserDetailsService userDetailsService;
    private final DateTimeProvider dateTimeProvider;

    private static final String ISSUER = "fiap-pay";

    public boolean validateToken(String token) {
        try {
            getDecodedJWT(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            log.warn("Token inválido! Mensagem: {}", e.getMessage());
            return false;
        }
    }

    private DecodedJWT getDecodedJWT(String token) throws UnsupportedEncodingException {
        var require = JWT.require(Algorithm.HMAC256(SECRET))
                .acceptExpiresAt(0)
                .build();
        var decodedJWT = require.verify(token);
        if (decodedJWT.getExpiresAt().before(new Date())) {
            throw new JWTVerificationException("Token expirado");
        }
        return decodedJWT;
    }

    public String generateToken(Usuarios usuario) {
        return createToken(usuario);
    }

    private String createToken(Usuarios usuario) {
        final LocalDateTime localDateTime = this.dateTimeProvider.dateTime();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(usuario.getLogin())
                .withExpiresAt(expirationDate())
                .withIssuedAt(this.dateTimeProvider.toDate(localDateTime))
                .sign(Algorithm.HMAC256(SECRET));
    }

    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).plusMinutes(2).toInstant();
    }

    public String getUsername(String token) throws UnsupportedEncodingException {
        final DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getSubject();
    }

    public Authentication getAuthentication(String token) throws UnsupportedEncodingException {
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
