package com.educhain.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "EduChainSecretKeyForJwtAuthentication2026Secure";

    private static final long JWT_EXPIRATION =
            1000 * 60 * 60 * 24;

    // ==========================================
    // Signing Key
    // ==========================================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ==========================================
    // Generate JWT Token
    // ==========================================

    public String generateToken(
            String email,
            String role
    ) {

        Date now = new Date();

        Date expiryDate =
                new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // ==========================================
    // Extract Email
    // ==========================================

    public String extractEmail(String token) {

        return getClaims(token)
                .getSubject();
    }

    // ==========================================
    // Extract Role
    // ==========================================

    public String extractRole(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    // ==========================================
    // Validate Token
    // ==========================================

    public boolean isTokenValid(
            String token,
            String email
    ) {

        try {

            Claims claims = getClaims(token);

            String tokenEmail =
                    claims.getSubject();

            Date expiration =
                    claims.getExpiration();

            return tokenEmail.equals(email)
                    && expiration.after(new Date());

        } catch (Exception e) {

            return false;
        }
    }

    // ==========================================
    // Get Claims
    // ==========================================

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}