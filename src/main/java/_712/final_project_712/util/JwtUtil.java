package _712.final_project_712.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    
    // 使用更安全的密钥生成方式
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    
    // token过期时间：1小时
    private final long expiration = 3600000;
    
    /**
     * 生成JWT token
     * @param userId 用户ID
     * @param username 用户名
     * @return JWT token字符串
     */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        return Jwts.builder()
                .setSubject(String.valueOf(userId))  // 设置用户ID为主题
                .claim("username", username)         // 添加用户名到claims
                .setIssuedAt(now)                   // 设置签发时间
                .setExpiration(expiryDate)          // 设置过期时间
                .signWith(key)                      // 使用密钥签名
                .compact();                         // 生成token字符串
    }
    
    /**
     * 从token中解析出Claims（包含token中的信息）
     * @param token JWT token字符串
     * @return Claims对象
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 验证token是否有效
     * @param token JWT token字符串
     * @return 如果token有效返回true，否则返回false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 从token中获取用户ID
     * @param token JWT token字符串
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }
    
    /**
     * 从token中获取用��名
     * @param token JWT token字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * 检查token是否过期
     * @param token JWT token字符串
     * @return 如果token已过期返回true，否则返回false
     */
    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }
} 