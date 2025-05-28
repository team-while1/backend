package while1.kunnect.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import while1.kunnect.domain.Comment;


import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //게시글id로 조회(작성시간 기준으로 오름차순 정렬)
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    //유저id로 조회
    List<Comment> findByMemberId(Long memberId);
}
