package while1.kunnect.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class PostUtils {

    private PostUtils() {}

    /**
     * 애플리케이션을 게시글에 추가하고 양방향 관계 설정
     * @param post 게시글
     * @param application 추가할 신청
     */
    public static void addApplication(Post post, Application application) {
        post.getApplications().add(application);
        // 양방향 관계 설정 (application의 post 필드가 이미 설정되지 않았을 경우)
        if (application.getPost() != post) {
            application.setPost(post);
        }
    }

    /**
     * 현재 신청자 수 계산
     * @param post 게시글
     * @return 신청자 수
     */
    public static int getParticipants(Post post) {
        return post.getApplications().size();
    }

    /**
     * 현재 승인된 신청자 수 계산
     * @param post 게시글
     * @return 승인된 신청자 수
     */
    public static int getApprovedParticipants(Post post) {
        return (int) post.getApplications().stream()
                .filter(app -> app.getStatus() == ApplicationStatus.APPROVED)
                .count();
    }

    /**
     * 모집 가능 여부 확인
     * @param post 게시글
     * @return 모집 가능 여부
     */
    public static boolean isApplicationAvailable(Post post) {
        return post.getStatus() == PostStatus.OPEN &&
                getApprovedParticipants(post) < post.getTotalSlots() &&
                LocalDate.now().isBefore(post.getEndDate());
    }

    /**
     * 게시글 마감 처리
     * @param post 게시글
     */
    public static void close(Post post) {
        post.setStatus(PostStatus.CLOSED);
    }

    /**
     * 게시글 완료 처리
     * @param post 게시글
     */
    public static void complete(Post post) {
        post.setStatus(PostStatus.COMPLETED);
    }

    /**
     * 게시글 취소 처리
     * @param post 게시글
     */
    public static void cancel(Post post) {
        post.setStatus(PostStatus.CANCELLED);
    }

    /**
     * 남은 자리 계산
     * @param post 게시글
     * @return 남은 자리 수
     */
    public static int getRemainingSlots(Post post) {
        return Math.max(0, post.getTotalSlots() - getApprovedParticipants(post));
    }

    /**
     * 마감일까지 남은 일수 계산
     * @param post 게시글
     * @return 남은 일수
     */
    public static long getRemainingDays(Post post) {
        LocalDate today = LocalDate.now();
        if (today.isAfter(post.getEndDate())) {
            return 0;
        }
        return ChronoUnit.DAYS.between(today, post.getEndDate());
    }
}