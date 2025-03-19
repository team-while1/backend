package while1.kunnect.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import while1.kunnect.domain.Member;
import while1.kunnect.dto.MemberDto;
import while1.kunnect.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/{member_id}")
    public MemberDto getMember(@PathVariable(name="member_id") Long memberId) {
        Member member = memberService.findById(memberId);
        return MemberDto.from(member);
    }

}
