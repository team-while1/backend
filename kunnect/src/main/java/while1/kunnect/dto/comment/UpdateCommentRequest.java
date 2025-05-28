package while1.kunnect.dto.comment;

import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
public class UpdateCommentRequest {
    private Long memberId;
    @NotNull
    private String content;
}
