package while1.kunnect.config.jwt;

import java.security.Key;
import java.time.Duration;
import java.util.*;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import static java.util.Collections.emptyMap;

@Getter
public class JwtFactory {
    private String subject = "test@test.com";
    private Date issuedAt = new Date();
    private Date expiration = new Date(issuedAt.getTime() + Duration.ofDays(14).toMillis());
    private Map<String, Object> claims = emptyMap();

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    // jjwt 라이브러리 사용한 JWT 토큰 생성
    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
//                .signWith(SignatureAlgorithm.ES256, jwtProperties.getSecretKey())
//                      --> 비대칭키 알고리즘(ECDSA, RSA-256해시 함수)이지만 Key객체를 같이 넘겨줘야 해서 """우선 HS256(HMAC알고리즘) 사용"""
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }
}
