package while1.kunnect.dto.reply;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyRequestDto {
    private Long commentId;
    private Long memberId;
    private String content;
}