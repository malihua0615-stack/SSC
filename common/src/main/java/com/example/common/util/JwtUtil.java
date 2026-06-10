package com.example.common.util;

import com.example.common.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY_STRING = "your_very_long_and_secure_secret_key_string_here_which_is_at_least_32_bytes";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
    public static String getToken(UserEntity userEntity) {
        return Jwts.builder()
                .subject(userEntity.getId())
                .claim("username",userEntity.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 86400000))
                .signWith(KEY)
                .compact();
    }
}
