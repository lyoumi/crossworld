package com.cwd.tg.gss.security;

import com.cwd.tg.gss.errors.exceptions.TokenExpirationException;
import com.cwd.tg.gss.errors.exceptions.TokenValidationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private static final String PERMISSIONS = "permissions";

    @Value("${jwt.secret}")
    private String secret;

    User getUserFromToken(String token) {
        var claims = getAllClaimsFromToken(token);
        return new User(claims.getSubject(), Set.copyOf((List<String>) claims.get(PERMISSIONS)));
    }

    Boolean isTokenExpired(String token) {
        var expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date(System.currentTimeMillis()));
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        var claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            throw new TokenValidationException("Existing token is invalid", e);
        } catch (ExpiredJwtException e) {
            throw new TokenExpirationException("Existing token is expired", e);
        }
    }
}
