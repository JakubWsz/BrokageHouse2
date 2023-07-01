package com.brokagehouse.authservice.utils;

import com.brokagehouse.authservice.model.UserAuth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil implements ApplicationRunner {

    public static final int TO_SECONDS = 1000;
    @Value("${springbootwebfluxjjwt.jjwt.secret}")
    private String secret;

    @Value("${springbootwebfluxjjwt.jjwt.expiration}")
    private long expirationTimeSeconds;

    private Key key;

    @Override
    public void run(ApplicationArguments args) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        return isTokenExpired(token);
    }

    public String generateToken(UserAuth userAuth) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userAuth.getRoles());
        return generateToken(claims, userAuth.getUsername());
    }

    private Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).after(new Date());
    }

    private String generateToken(Map<String, Object> claims, String username) {
        Date creationDate = new Date();
        Date expirationDate = new Date(creationDate.getTime() * TO_SECONDS + expirationTimeSeconds);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }
}
