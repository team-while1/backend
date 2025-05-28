package while1.kunnect.config.jwt;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;
import while1.kunnect.domain.Member;
import while1.kunnect.repository.MemberRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class JwtFactoryTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("generateToken() 검증")
    @Test
    void generateToken() {
        // given
        Member testMember = memberRepository.save(Member.builder()
                .email("test@example.com")
                .password("1233")
                .major(Major.MECHANICAL_ENGINEERING)
                .college(College.CONVERGENCE_TECHNOLOGY)
                .studentNum(1231L)
                .name("test1")
                .build());
        // when
        String token = tokenProvider.generateToken(testMember, Duration.ofDays(14));
        // then :
        // jjwt라이브러리 사용하여 토큰 복호화. 토큰 생성 시 넣어둔 id값(given절)이 유저ID와 동일한가
        Long memberId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);
        assertThat(memberId).isEqualTo(testMember.getId());
    }

    @DisplayName("validToken() 검증 실패")
    @Test
    void invalidToken() {
        // given : 이미 만료된 토큰 생성
        String token = JwtFactory.builder()
                .expiration(new Date(
                        new Date().getTime() - Duration.ofDays(7).toMillis())
                )
                .build()
                .createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validToken(token);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("validToken() 검증 성공")
    @Test
    void validToken() {
        // given : 유효한 토큰 생성
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validToken(token);
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication() 검증")
    @Test
    void getAuthentication() {
        // given
        String memberEmail = "test@example.com";
        String token = JwtFactory.builder()
                .subject(memberEmail)
                .build()
                .createToken(jwtProperties);
        // when
        Authentication authentication = tokenProvider.getAuthentication(token);
        // then
        assertThat( ((UserDetails) authentication.getPrincipal()).getUsername())
                .isEqualTo(memberEmail);
    }

    @DisplayName("getMemberId() 검증")
    @Test
    void getMemberId() {
        // given
        Long memberId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", memberId))
                .build()
                .createToken(jwtProperties);
        // when
        Long memberIdByToken = tokenProvider.getUserIdFromToken(token);
        // then :
        // 토큰 기반으로 유저ID를 가져왔을 때 같은지
        assertThat(memberIdByToken).isEqualTo(memberId);
    }

}