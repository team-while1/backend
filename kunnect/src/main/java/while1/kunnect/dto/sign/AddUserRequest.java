package while1.kunnect.dto.sign;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;

@Getter
public class AddUserRequest {
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    private String email;

    @NotBlank(message = "별명은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @JsonProperty("student_num")
    @NotBlank(message = "학번은 필수 입력값입니다.")
    private String studentNum;

    @NotNull(message = "소속은 필수 입력값입니다.")
    private College college;

    @NotNull(message = "전공은 필수 입력값입니다.")
    private Major major;
}
