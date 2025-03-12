package while1.kunnect.dto.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenRequest {
    private String refreshToken;
}
