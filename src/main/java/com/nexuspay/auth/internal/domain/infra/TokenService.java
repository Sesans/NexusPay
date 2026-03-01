package com.nexuspay.auth.internal.domain.infra;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nexuspay.auth.internal.domain.model.User;
import com.nexuspay.common.infra.TokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService implements TokenValidator {
    @Value("${api.security.token}")
    private String secret;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("nexuspay")
                    .withSubject(user.getId().toString())
                    .withClaim("status", user.getStatus().toString())
                    .withClaim("email", user.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error while creating validation token!", exception);
        }
    }

    public DecodedJWT validateAndDecodeToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("nexuspay")
                    .build()
                    .verify(token);
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Error while validating token!");
        }
    }

    private Instant generateExpirationDate(){
        return Instant.now().plus(15, ChronoUnit.MINUTES);
    }

    @Override
    public UUID validateAndGetSubject(String token) {
        try {
            DecodedJWT decoded = validateAndDecodeToken(token);
            return UUID.fromString(decoded.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        try {
            DecodedJWT decoded = validateAndDecodeToken(token);
            String status = decoded.getClaim("status").asString();
            // Transform the status (VERIFIED, PENDING) in a spring role
            return List.of(new SimpleGrantedAuthority("ROLE_" + status));
        } catch (Exception e) {
            return List.of();
        }
    }
}