package while1.kunnect.entity;

public enum PostStatus {
    OPEN("모집"),
    CLOSED("마감"),
    COMPLETED("완료"),
    CANCELLED("취소");

    private final String status;

    PostStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}