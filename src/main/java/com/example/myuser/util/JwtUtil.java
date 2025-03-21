package com.example.myuser.util;

import com.example.myuser.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "ma-cle-secrete-tres-longue-et-securisee-123456789"; // Doit être longue pour HS256
    private static final long EXPIRATION_TIME = 3600 * 1000; // 1 heure
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 3600 * 1000; // 7 jours

    public static String generateToken(String username, Role role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                //.signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public static Role getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String roleString = claims.get("role", String.class);
            return Role.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            return Role.ROLE_VISITOR; // Rôle par défaut
        }
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

