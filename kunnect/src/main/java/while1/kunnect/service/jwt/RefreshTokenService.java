package while1.kunnect.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import while1.kunnect.domain.RefreshToken;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;
import while1.kunnect.repository.jwt.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESHTOKEN_NOT_FOUND));
    }
}
