package br.com.mywallet.app.domain.service;

import br.com.mywallet.app.domain.model.Usuario.Usuario;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Usuario usuario) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT
                    .create()
                    .withIssuer("skooby-doo-livro")
                    .withSubject(usuario.getEmail())
                    .withClaim("role", usuario.getRole().name())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            throw new JWTTokenException("Erro ao gerar o token JWT.");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("skooby-doo-livro")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03" +
                ":00"));
    }
}
