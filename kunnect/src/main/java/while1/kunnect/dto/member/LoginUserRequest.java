package while1.kunnect.dto.member;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest (
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    String email,

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    String password
) {}
