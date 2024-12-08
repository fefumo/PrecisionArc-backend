package se.ifmo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import java.util.Base64;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = Base64.getEncoder().encodeToString("my_secret_key".getBytes());

    public static String generateToken(String username) {
        return Jwts.builder()
                   .setSubject(username)
                   .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                   .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                   .compact();
    }

    public static String extractUsername(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(SECRET_KEY)
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
