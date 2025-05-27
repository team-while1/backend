package while1.kunnect.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import while1.kunnect.domain.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member writer;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    private int views;
    private int likes;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ********** 수정 전 (archived) *********
    // private Boolean archived;        // 모집 완료 여부
    // ********** 수정 후 (status)   *********
    @Enumerated(EnumType.STRING)
    private PostStatus status;       // 모집 상태
    private LocalDate startDate;     // 모집 시작일
    private LocalDate endDate;       // 모집 마감일
    private int totalSlots;          // 모집 인원
    private String categoryId;       // 카테고리

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Application> applications = new ArrayList<>();

    public void changeStatus(PostStatus status) {
        this.status = status;
    }

    public void addApplication(Application application) {
        this.applications.add(application);
        if (application.getPost() != this) application.setPost(this);
    }
}
