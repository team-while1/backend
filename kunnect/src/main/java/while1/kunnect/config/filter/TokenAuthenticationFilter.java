package while1.kunnect.config.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import while1.kunnect.config.jwt.TokenProvider;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREPIX = "Bearer ";

    // permitAll 경로들 정의
    private static final List<String> PERMIT_ALL_PATHS = Arrays.asList(
            "/api/signup",
            "/api/login",
            //"/api/member",
            "/api/auth/find"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // FIXME permitAll 경로는 JWT 검증 건너뛰기 -> 없어도 됨. securityFilterChain에서 확인함.
        /* if (shouldSkipFilter(requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }
        */

        // 요청 헤더 Authorization 키 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        // 접두사 제거
        String token = getAccessToken(authorizationHeader);
        // 토큰 유효성 검사, 완료 시 인증 정보 설정
        if (tokenProvider.validToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREPIX)) {
            return authorizationHeader.substring(TOKEN_PREPIX.length());
        }
        return null;
    }

    // FIXME securityFilterChain에서 확인함.
    /**
     * JWT 검증을 건너뛸 경로인지 확인
     */
    private boolean shouldSkipFilter(String requestURI) {
        return PERMIT_ALL_PATHS.stream()
                .anyMatch(path -> requestURI.startsWith(path)) ||
                requestURI.startsWith("/static/") ||
                requestURI.equals("/") ||
                requestURI.startsWith("/images/");
    }
}