package hello.hello_spring.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

@Component
public class JwtUtil {

    // application.properties에서 jwt.secret.key 값을 주입받음
    @Value("${jwt.secret.key}")
    private String secretKey;

    // 비밀 키를 가져오는 메서드
    private SecretKey getSecretKey() {
        // secretKey가 null이거나 비어있을 경우 예외를 던짐
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("JWT 비밀 키(secretKey)가 설정되지 않았습니다.");
        }
        // 비밀 키를 SecretKey 객체로 변환하여 반환
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 유효
                .signWith(getSecretKey()) // 비밀 키 사용
                .compact();
    }

    // 토큰에서 사용자 이름 추출
    public String extractUsername(String token) {
        try {
            return extractClaims(token).getSubject();
        } catch (Exception e) {
            System.out.println("JWT 토큰에서 username 추출 오류: " + e.getMessage());
            throw new RuntimeException("JWT 토큰에서 username 추출 실패", e);
        }
    }


    // 토큰이 만료되었는지 확인
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        try {
            // 토큰에서 'Bearer ' 접두사를 제거
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            System.out.println("Parsing token: " + token);

            // JWT 토큰 파싱
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey()) // 비밀 키 사용
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 토큰이 만료되었습니다.");
            throw new RuntimeException("JWT 토큰이 만료되었습니다.", e);
        } catch (Exception e) {
            System.out.println("JWT 파싱 오류: " + e.getMessage());
            throw new RuntimeException("JWT 토큰 파싱 실패", e);
        }
    }

}