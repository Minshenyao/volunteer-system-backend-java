package edu.ahut.volunteersystembackend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtils {

    // 可以改为从配置加载密钥
    @Value("${jwt.secret}")
    private static String jwtSecret;

    private static final Key SECRET_KEY = getKeyFromConfiguration();

    private static Key getKeyFromConfiguration() {
        String secret = jwtSecret;
        if (secret == null || secret.length() < 32) {
            // 默认生成密钥，但这不是生产环境推荐的做法
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        // 使用已有的密钥字符串
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 默认过期时间：24小时
    private static final long DEFAULT_EXPIRATION = 24 * 60 * 60 * 1000;

    /**
     * 创建JWT令牌
     * @param subject 主题（通常是用户ID或用户名）
     * @return JWT令牌字符串
     */
    public static String createToken(String subject) {
        return createToken(subject, new HashMap<>());
    }

    /**
     * 创建带有自定义声明的JWT令牌
     * @param subject 主题
     * @param claims 自定义声明
     * @return JWT令牌字符串
     */
    public static String createToken(String subject, Map<String, Object> claims) {
        return createToken(subject, claims, DEFAULT_EXPIRATION);
    }

    /**
     * 创建带有自定义声明和过期时间的JWT令牌
     * @param subject 主题
     * @param claims 自定义声明
     * @param expirationMillis 过期时间（毫秒）
     * @return JWT令牌字符串
     */
    public static String createToken(String subject, Map<String, Object> claims, long expirationMillis) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)  // 设置自定义声明
                .setSubject(subject)  // 设置主题
                .setIssuedAt(new Date(now))  // 设置签发时间
                .setExpiration(new Date(now + expirationMillis))  // 设置过期时间
                .signWith(SECRET_KEY)  // 使用密钥签名
                .compact();  // 生成JWT字符串
    }

    /**
     * 验证JWT令牌是否有效
     * @param token JWT令牌
     * @return 如果令牌有效返回true，否则返回false
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 获取JWT令牌中的主题
     * @param token JWT令牌
     * @return 主题
     */
    public static String getSubject(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * 获取JWT令牌中的过期时间
     * @param token JWT令牌
     * @return 过期时间
     */
    public static Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * 检查JWT令牌是否已过期
     * @param token JWT令牌
     * @return 如果已过期返回true，否则返回false
     */
    public static boolean isTokenExpired(String token) {
        Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    /**
     * 从JWT令牌中获取特定的自定义声明
     * @param token JWT令牌
     * @param claimName 声明名称
     * @return 声明值
     */
    public static Object getClaim(String token, String claimName) {
        Claims claims = getAllClaims(token);
        return claims.get(claimName);
    }

    /**
     * 从JWT令牌中获取所有声明
     * @param token JWT令牌
     * @return 所有声明
     */
    public static Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从JWT令牌中获取指定的声明
     * @param token JWT令牌
     * @param claimsResolver 声明解析函数
     * @return 解析后的声明
     */
    public static <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }
}
