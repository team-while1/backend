package while1.kunnect.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import while1.kunnect.repository.MemberRepository;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtFactoryTest {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private MemberRepository memberRepository;

}