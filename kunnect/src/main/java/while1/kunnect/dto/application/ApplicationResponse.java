package while1.kunnect.dto.application;

import while1.kunnect.entity.Application;
import while1.kunnect.entity.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long postId,
        String postTitle,
        Long memberId,
        String memberName,
        ApplicationStatus status,
        String comment,
        LocalDateTime appliedAt,
        LocalDateTime processedAt
) {
    public ApplicationResponse(Application application) {
        this(
                application.getId(),
                application.getPost().getPostId(),
                application.getPost().getTitle(),
                application.getMember().getId(),
                application.getMember().getName(),
                application.getStatus(),
                application.getComment(),
                application.getApplyAt(),
                application.getProcessedAt()
        );
    }
}
