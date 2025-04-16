package while1.kunnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;

public record AddUserRequest (
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    String email,

    @NotBlank(message = "별명은 필수 입력값입니다.")
    String name,

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    String password,

    @JsonProperty("student_num")
    @NotBlank(message = "학번은 필수 입력값입니다.")
    @Pattern(regexp = "^\\d+$", message = "학번은 숫자만 입력 가능합니다.")
    String studentNum,

    @NotNull(message = "소속은 필수 입력값입니다.")
    College college,

    @NotNull(message = "전공은 필수 입력값입니다.")
    Major major
) {}
