package com.lew.server.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class GeneratorTokenUtils {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成Token
     * @param userDetails
     * @return
     */
    public String generatorToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());

        return generatorToken(claims);
    }

    /**
     * 从Token中获取用户名
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token){
        String username;
        try {
            Claims claims = parseToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }

        return username;
    }

    /**
     * 验证Token是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public boolean isValidateToken(String token,UserDetails userDetails){
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername())&& !isTokenExpired(token);
    }

    /**
     * 判断Token能否被刷新
     * @param token
     * @return
     */
    public boolean canRefreshToken(String token){
        return !isTokenExpired(token);
    }

    /**
     * 刷新Token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = parseToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generatorToken(claims);
    }

    /**
     * 判断Token是否失效
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        Date expireDate = getExpirationFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * 从Token中获取失效时间
     * @param token
     * @return
     */
    private Date getExpirationFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    /**
     * 获取Token中的负载
     * @param token
     * @return
     */
    private Claims parseToken(String token) {
         Claims claims = null;
        try {
            claims = Jwts.parser()
                   .setSigningKey(secret)
                   .parseClaimsJws(token)
                   .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return claims;
    }

    /**
     * 根据负载生成Token
     * @param claims
     * @return
     */
    private String generatorToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256,secret)
                .setExpiration(generatorTokenExpirationDate())
                .compact();
    }

    /**
     * 获取失效最终时间
     * @return
     */
    private Date generatorTokenExpirationDate() {
        return new Date(System.currentTimeMillis()+expiration*1000);
    }
}
