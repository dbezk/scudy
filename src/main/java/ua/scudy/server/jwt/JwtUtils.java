package ua.scudy.server.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    public final Algorithm getJwtMainAlgorithm() {
        return Algorithm.HMAC256(jwtSecretKey.getBytes());
    }

    public final JWTVerifier getJwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm).build();
    }

    public final String generateUserAccessToken(String tokenSubject, String issuerUrl, Collection<GrantedAuthority> authorities) {
        return JWT.create()
                .withSubject(tokenSubject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 12 * 60 * 60 * 1000))
                .withIssuer(issuerUrl)
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(getJwtMainAlgorithm());
    }

    public final String generateUserRefreshToken(String tokenSubject, String issuerUrl) {
        return JWT.create()
                .withSubject(tokenSubject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withIssuer(issuerUrl)
                .sign(getJwtMainAlgorithm());
    }



}
