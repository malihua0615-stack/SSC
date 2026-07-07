package com.example.common.util;

import com.example.common.entity.UserEntity;
import com.example.common.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class JwtUtil {

    private static final String SECRET = "your_very_long_and_secure_secret_key_string_here_which_is_at_least_32_bytes";

    private static final Long EXPIRE = 600000L;

    private static SecretKey getSecretKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        // 如果密钥长度不足 32 字节，Keys.hmacShaKeyFor 会自动补足
        // 建议配置文件里直接把密钥写够 32 位以上

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String getToken(UserEntity userEntity) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userEntity.getId())
                .claim("username",userEntity.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ EXPIRE))
                .signWith(getSecretKey())
                .compact();
    }

    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("Token已过期: {}", e.getMessage());
            throw new BusinessException(401, "已过期，请重新登录");
        } catch (MalformedJwtException e) {
            log.warn("Token格式错误: {}", e.getMessage());
            throw new BusinessException(401, "Token格式错误");
        } catch (SignatureException e) {
            log.warn("Token签名验证失败: {}", e.getMessage());
            throw new BusinessException(401, "Token签名验证失败");
        } catch (IllegalArgumentException e) {
            log.warn("Token参数异常: {}", e.getMessage());
            throw new BusinessException(401, "Token参数异常");
        } catch (Exception e) {
            log.error("Token验证异常: {}", e.getMessage(), e);
            throw new BusinessException(401, "Token无效，请重新登录");
        }
    }

    public static String getJit(String token) {
        return parseToken(token).getId();
    }

    public static String getId(String token) {
        return parseToken(token).getSubject();
    }

    public static Date getExpire(String token) {
        return parseToken(token).getExpiration();
    }

    public static boolean validateToken(String token) {
        try{
            parseToken(token);
            return true;
        }catch (Exception e){
            throw new BusinessException(500,"Token 验证失败"+e.getMessage());
        }
    }
}
