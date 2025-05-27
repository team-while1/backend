package while1.kunnect.entity;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절");
    // "취소"는 데이터를 지워버리면 됨.

    private final String status;

    ApplicationStatus(String status) {
        this.status = status;
    }

}