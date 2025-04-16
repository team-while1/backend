package while1.kunnect.controller;

import java.time.Duration;
import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import while1.kunnect.config.jwt.TokenProvider;
import while1.kunnect.domain.Member;
import while1.kunnect.dto.FindIdRequest;
import while1.kunnect.dto.MemberDto;
import while1.kunnect.dto.UpdatePasswordRequest;
import while1.kunnect.dto.sign.AddUserRequest;
import while1.kunnect.dto.sign.LoginUserRequest;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;
import while1.kunnect.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AddUserRequest request) {
        if (memberService.checkDuplicationForEmail(request.email())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (memberService.checkDuplicationForName(request.name())) {
            throw new CustomException(ErrorCode.NAME_ALREADY_EXISTS);
        }
        memberService.save(request);
        return ResponseEntity.ok().body(Map.of("message", "회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserRequest request) {
        Member member = memberService.getMemberByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        String accessToken = tokenProvider.generateToken(member, Duration.ofHours(1));
        String refreshToken = tokenProvider.generateToken(member, Duration.ofDays(7));
        Map<String, String> tokens = memberService.saveAndGetTokens(member, accessToken, refreshToken);
        return ResponseEntity.ok().body(tokens);
    }

    @PostMapping("/find/id")
    public ResponseEntity<?> findId(@RequestBody @Valid FindIdRequest request) {
        Member member = memberService.findEmail(request.studentNum());
        return ResponseEntity.ok().body(MemberDto.from(member));
    }

    @PostMapping("/find/pw-change")
    public ResponseEntity<?> changePassword(@RequestBody @Valid UpdatePasswordRequest request) {
        memberService.updatePassword(request);
        String message = "비밀번호 변경 성공";
        return ResponseEntity.ok().body(Map.of("message", message));
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam @Valid @NotNull @Email String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        if (memberService.checkDuplicationForEmail(email)) {
            return ResponseEntity.ok().body(Map.of("is_available", false));
        }
        return ResponseEntity.ok().body(Map.of("is_available", true));
    }

    @GetMapping("/check-name")
    public ResponseEntity<?> checkName(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        if (memberService.checkDuplicationForName(name)) {
            return ResponseEntity.ok().body(Map.of("is_available", false));
        }
        return ResponseEntity.ok().body(Map.of("is_available", true));
    }

}
