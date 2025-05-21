package while1.kunnect.entity;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import while1.kunnect.domain.Member;

@Entity
@Getter
@Setter
@NoArgsConstructor                        // Hibernate, JSON 직렬화/역직렬화 시 필요
@AllArgsConstructor                       // Builder 패턴과 함께 사용
@Builder                                  // 불변 객체를 생성할 때 특히 유용
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                      // 신청 고유 식별자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")         // 컬럼명 지정
    private Post post;                    // 연관된 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")       // 컬럼명 지정
    private Member member;                // 신청자 (회원)

    @CreationTimestamp
    private LocalDateTime applyAt;        // 신청 시간
    @UpdateTimestamp
    private LocalDateTime modifyAt;       // 수정 시간
    private LocalDateTime processedAt;    // 신청 처리 시점 (승인/거절 시점)

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status      // 신청 상태 (대기, 승인, 거절)
          = ApplicationStatus.PENDING;;

    private String comment;               // 신청 시 남긴 코멘트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private Member processedBy;           // 신청을 처리한 관리자/게시글 작성자 (선택적)

    // 추가될 수 있는 변수들
    private Integer priority;             // 우선순위 (대기열 관리용)
    private Boolean notified;             // 알림 발송 여부


    public void changeStatus(ApplicationStatus newStatus) {
        if (this.status == ApplicationStatus.PENDING && newStatus != ApplicationStatus.PENDING) {
            this.processedAt = LocalDateTime.now();
        }
        this.status = newStatus;
    }

    public void approve() {
        changeStatus(ApplicationStatus.APPROVED);
    }

    public void reject(String reason) {
        changeStatus(ApplicationStatus.REJECTED);
    }
}