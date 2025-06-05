package while1.kunnect.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordRequest(
        @Email
//        @NotBlank(message = "이메일은 필수 입력값입니다.")
        String email,
//        @NotBlank(message = "별명은 필수 입력값입니다.")
        String name,
        @JsonProperty("student_num")
        @NotBlank(message = "학번은 필수 입력값입니다.")
        @Pattern(regexp = "^\\d+$", message = "학번은 숫자만 입력 가능합니다.")
        String studentNum,
        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        String password
)
{}