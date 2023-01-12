package com.github.Ksionzka.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    private final static String ISSUER = "Ksionzka";

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(Long id) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
            .withSubject("User Details")
            .withClaim("id", id)
            .withIssuedAt(Instant.now())
            .withIssuer(ISSUER)
            .sign(Algorithm.HMAC256(secret));
    }

    public Long validateTokenAndRetrieveUserId(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
            .withSubject("User Details")
            .withIssuer(ISSUER)
            .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("id").asLong();
    }
}
