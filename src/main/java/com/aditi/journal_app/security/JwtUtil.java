package com.aditi.journal_app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationMs = 86400000; // 1 day

    // Generate JWT from username + roles
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)                  // who the token is about
                .claim("roles", roles)                 // custom claim = user roles
                .setIssuedAt(new Date())               // when issued
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs)) // expiry
                .signWith(key)                         // digitally sign with secret key
                .compact();                            // return as string
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
//    generateToken() → "Make me a new entry pass for this user, valid for 1 day."
//    extractUsername() → "Read the username from the entry pass."
//    validateToken() → "Check if this entry pass is still valid and not fake."
}

