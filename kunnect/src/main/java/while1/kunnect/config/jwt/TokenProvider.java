package while1.kunnect.config.jwt;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import while1.kunnect.domain.Member;


@Service
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(Member member, Duration expiredAt){
        Date now = new Date();
        return makeToken(member, now, new Date(now.getTime() + expiredAt.toMillis()));
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Member member, Date now, Date expiry) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(member.getEmail())
                .claim("id", member.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // JWT 토큰 유효성 검사 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 복호화
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.info("invalid token : {}", token);
            return false;
        }
    }

    // 인증 정보 가져오기 : 토큰 기반 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.
                singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(
                new User(claims.getSubject(), "" , authorities),
                token,
                authorities
        );
    }


    // 유저 ID 가져오기 : 토큰 기반 메서드
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

}
