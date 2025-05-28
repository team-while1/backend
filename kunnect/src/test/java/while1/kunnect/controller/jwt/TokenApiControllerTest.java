package while1.kunnect.controller.jwt;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import while1.kunnect.config.jwt.JwtFactory;
import while1.kunnect.config.jwt.JwtProperties;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;
import while1.kunnect.domain.Member;
import while1.kunnect.domain.RefreshToken;
import while1.kunnect.dto.jwt.CreateAccessTokenRequest;
import while1.kunnect.repository.MemberRepository;
import while1.kunnect.repository.jwt.RefreshTokenRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // ?
class TokenApiControllerTest {
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired private WebApplicationContext wac;
    @Autowired JwtProperties jwtProperties;
    @Autowired MemberRepository memberRepository;
    @Autowired RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        memberRepository.deleteAll();
    }

    @DisplayName("createNewAccessToken 검증")
    @Test
    public void createNewAccessToken() throws Exception {
        // given :
        // 테스트 유저 생성, refresh token 생성 & DB 저장, 요청 객체 생성
        final String url = "/token";

        Member testMember = memberRepository.save(Member.builder()
                .email("test@test.com")
                .name("test")
                .password("test")
                .major(Major.MECHANICAL_ENGINEERING)
                .college(College.CONVERGENCE_TECHNOLOGY)
                .studentNum(123L)
                .build());
        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testMember.getId()))
                .build()
                .createToken(jwtProperties);
        refreshTokenRepository.save(new RefreshToken(testMember.getId(), refreshToken));

        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);
        final String requestBody = objectMapper.writeValueAsString(request);

        // when :
        // post요청을 토큰 추가API에 보낸다.
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));
        // then :
        // 응답코드 201(Created)인지 확인 & 액세스 토큰 비어있지 않은지 확인
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}