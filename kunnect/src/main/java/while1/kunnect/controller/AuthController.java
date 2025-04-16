package while1.kunnect.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import while1.kunnect.domain.Member;
import while1.kunnect.dto.MemberDto;
import while1.kunnect.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
/**
 * 인가가 필요한 경로 : /api/auth
 * 들은 여기에, 인가가 필요없는 경로 : /api
 * 들은 MemberController에.
 */
public class AuthController {
    private final MemberService memberService;


    @GetMapping("/logout") // TODO : refresh token은 제거 했지만 발급된 토큰에 대해서 무효화시키기 가능한가 (refer : https://engineerinsight.tistory.com/232)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        memberService.logout(request, response);
        return ResponseEntity.ok().body(Map.of("message", "로그아웃 성공"));
    }



    @GetMapping("/member/{member_id}")
    public MemberDto getMember(@PathVariable(name="member_id") Long memberId) {
        Member member = memberService.findById(memberId);
        return MemberDto.from(member);
    }

    @DeleteMapping("/member/{member_id}")
    public ResponseEntity<?> deleteMember(@PathVariable(name="member_id") Long memberId) {
        memberService.deleteById(memberId);
        return ResponseEntity.ok().body(Map.of("message", "회원 삭제 성공"));
    }

}
