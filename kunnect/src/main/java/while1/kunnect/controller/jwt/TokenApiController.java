package while1.kunnect.controller.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import while1.kunnect.dto.jwt.CreateAccessTokenRequest;
import while1.kunnect.dto.jwt.CreateAccessTokenResponse;
import while1.kunnect.service.jwt.TokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken
            (@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService
                .createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
