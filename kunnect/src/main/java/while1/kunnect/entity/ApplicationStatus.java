package while1.kunnect.entity;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절"),
    CANCELED("취소");

    private final String status;

    ApplicationStatus(String status) {
        this.status = status;
    }

}