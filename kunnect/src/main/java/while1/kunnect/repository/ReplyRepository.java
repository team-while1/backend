package while1.kunnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import while1.kunnect.domain.Reply;


import java.util.List;


//특정 댓글에 달린 대댓글을 오래된 순으로 가져오기
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentIdOrderByCreatedAtAsc(Long commentId);
}
