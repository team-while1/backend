package while1.kunnect.service.jwt;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import while1.kunnect.domain.Member;
import while1.kunnect.domain.RefreshToken;
import while1.kunnect.config.jwt.TokenProvider;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;
import while1.kunnect.service.MemberService;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    public String createNewAccessToken(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new CustomException(ErrorCode.REFRESHTOKEN_NOT_FOUND);
        }
        Long memberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member member = memberService.findById(memberId);

        return tokenProvider.generateToken(member, Duration.ofHours(2));
    }

}
