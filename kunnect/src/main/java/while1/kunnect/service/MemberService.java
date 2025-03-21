package while1.kunnect.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public void logout(String accessToken) {
        Long memberId = tokenProvider.getUserIdFromToken(accessToken);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.setRefreshToken(null);
    }

    public void deleteById(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        memberRepository.deleteById(memberId);
    }
}
