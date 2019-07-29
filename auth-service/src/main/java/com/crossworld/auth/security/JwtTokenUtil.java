package com.crossworld.auth.security;

import com.crossworld.auth.data.CWUser;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

public interface JwtTokenUtil {

    String getUsernameFromToken(String token);

    Date getExpirationDateFromToken(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    String generateToken(CWUser userDetails);

    Boolean isTokenExpired(String token);
}
