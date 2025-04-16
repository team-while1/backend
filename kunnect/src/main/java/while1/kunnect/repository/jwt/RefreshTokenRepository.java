package while1.kunnect.repository.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import while1.kunnect.domain.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long memberId);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
