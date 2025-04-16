package while1.kunnect.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import while1.kunnect.config.jwt.TokenProvider;
import while1.kunnect.domain.Member;
import while1.kunnect.domain.enumtype.Role;
import while1.kunnect.dto.sign.AddUserRequest;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;
import while1.kunnect.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Long save(AddUserRequest dto) {
        return memberRepository.save(Member.builder()
                .email(dto.email())
                .name(dto.name())
                .password(bCryptPasswordEncoder.encode(dto.password()))
                .major(dto.major())
                .college(dto.college())
                .studentNum(Long.valueOf(dto.studentNum()))
                .role(Role.USER)
                .build())
        .getId();
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member getMemberByName(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public boolean checkDuplicationForEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean checkDuplicationForName(String name) {
        return memberRepository.existsByName(name);
    }

    @Transactional
    public Map<String, String> saveAndGetTokens(Member member, String accessToken, String refreshToken) {
        member.setRefreshToken(refreshToken);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        Object principal = auth.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else if (principal instanceof String) {
            username = (String)principal;
        } else {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.setRefreshToken(null);
        new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    public void deleteById(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        memberRepository.deleteById(memberId);
    }
}
