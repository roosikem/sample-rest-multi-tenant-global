package com.lkup.accounts.context.jwt;

import com.lkup.accounts.document.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.ms}")
    private long expiration;

    public String issueToken(String username, Role role) {
        return issueToken(username, Map.of(
                "role", role.getName(),
                "permissions", new ArrayList<>(role.getGrantedAuthorities())
        ));
    }

    public String issueToken(String subject, Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(expiration, ChronoUnit.HOURS)))
                .signWith(getSigningKey())
                .compact();
    }

    public String getSubject(String token) {
        Claims claims =   getClaims(token);
        return getClaims(token).getSubject();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public boolean isTokenValid(String jwt, String username) {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        Date today = Date.from(Instant.now());
        return getClaims(jwt).getExpiration().before(today);
    }

    private Claims getClaims(String token) {
        JwtParser build = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
        Jws<Claims> claimsJws = build.parseClaimsJws(token);
        return claimsJws.getBody();
    }
}
