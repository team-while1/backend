package while1.kunnect.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import while1.kunnect.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("member_id")
    private Long memberId;

    private String content;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("is_anonymous")
    private boolean isAnonymous;

    @JsonProperty("writer_name")
    private String writerName;

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .memberId(comment.getMemberId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .isAnonymous(comment.isAnonymous())
                .writerName(
                        comment.getMember() != null ? comment.getMember().getName() : "알 수 없음"
                )
                .build();
    }
}