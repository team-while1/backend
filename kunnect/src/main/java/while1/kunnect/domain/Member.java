package while1.kunnect.domain;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;
import while1.kunnect.domain.enumtype.Role;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // TODO : @NoArgsConstructor와 @AllArgsConstructor -> @Builder 찾아보기
@JsonIgnoreProperties({"hibernateLAzyInitializer", "handler", "member"}) // hibernate프록시 무시
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    @JsonIgnore // api응답에서 비밀번호 필드 제외
    private String password;

    @Column(name = "create_at", updatable = false) // 회원가입 시 자동 생성(수정 불가)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Column(name = "student_num", unique = true, nullable = false)
    private Long studentNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private College college;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Major major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    private String profileUrl = "/images/profile/anonymous.png";

    private String refreshToken;
}
