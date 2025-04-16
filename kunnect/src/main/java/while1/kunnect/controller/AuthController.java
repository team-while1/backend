package while1.kunnect.controller;

import java.security.Principal;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import while1.kunnect.domain.Member;
import while1.kunnect.dto.MemberDto;
import while1.kunnect.dto.MemberUpdateDto;
import while1.kunnect.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final MemberService memberService;


    // @AuthenticationPrincipal
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        memberService.logout(request, response);
        return ResponseEntity.ok().body(Map.of("message", "로그아웃 성공"));
    }

    @GetMapping("/member")
    public MemberDto getMember(HttpServletRequest request, HttpServletResponse response) {
        Member member = memberService.findMember(request, response);
        return MemberDto.from(member);
    }

    @DeleteMapping("/member")
    public ResponseEntity<?> deleteMember(HttpServletRequest request, HttpServletResponse response) {
        memberService.deleteById(request, response);
        return ResponseEntity.ok().body(Map.of("message", "회원 삭제 성공"));
    }

    @PutMapping("/member")
    public ResponseEntity<?> updateMember(@Valid @RequestBody MemberUpdateDto request) {
        Member member = memberService.updateMember(request);
        return ResponseEntity.ok().body(Map.of("message", "회원 정보 수정 성공", "member", MemberDto.from(member)));
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(Principal principal, UserDetails userDetails) {
        String a = principal.getName();
        String b = ((User)userDetails).getUsername();
        Collection<? extends GrantedAuthority> authorities = ((User)userDetails).getAuthorities();
        Map<String, Object> message = new HashMap<>();
        message.put("principal", a);
        message.put("userDetails", b);
        message.put("authorities", authorities);
        return ResponseEntity.ok().body(Map.of("message",message));
    }
}
