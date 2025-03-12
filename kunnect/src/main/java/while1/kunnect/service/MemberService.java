package while1.kunnect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import while1.kunnect.domain.Member;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;
import while1.kunnect.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
