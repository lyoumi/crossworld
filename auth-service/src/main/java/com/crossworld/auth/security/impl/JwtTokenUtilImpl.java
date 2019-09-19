package com.crossworld.auth.security.impl;

import com.crossworld.auth.data.User;
import com.crossworld.auth.errors.exceptions.TokenExpirationException;
import com.crossworld.auth.errors.exceptions.TokenValidationException;
import com.crossworld.auth.security.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtilImpl implements JwtTokenUtil {

    private static final long ONE_MINUTE_IN_MILLIS = 60000;
    private static final long EXPIRATION_TIME_MULTIPLIER = 30;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        var claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims, user.getUsername());
    }

    @Override
    public Boolean isTokenExpired(String token) {
        var expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date(System.currentTimeMillis()));
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

    private String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ONE_MINUTE_IN_MILLIS * EXPIRATION_TIME_MULTIPLIER))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}
