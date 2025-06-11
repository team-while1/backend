package while1.kunnect.dto.comment;

import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@Getter
public class UpdateCommentRequest {

    @NotBlank
    private String content;
}