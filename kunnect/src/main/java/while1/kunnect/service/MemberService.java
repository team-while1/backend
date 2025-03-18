package while1.kunnect.service;

import java.util.HashMap;
import java.util.Map;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Long save(AddUserRequest dto) {
        return memberRepository.save(Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .major(dto.getMajor())
                .college(dto.getCollege())
                .studentNum(dto.getStudentNum())
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
}
