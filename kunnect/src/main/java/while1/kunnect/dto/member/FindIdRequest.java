package while1.kunnect.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record FindIdRequest (
        @JsonProperty("student_num")
        @NotBlank(message = "학번은 필수 입력값입니다.")
        @Pattern(regexp = "^\\d+$", message = "학번은 숫자만 입력 가능합니다.")
        String studentNum
)
{}