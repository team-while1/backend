package while1.kunnect.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import while1.kunnect.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    List<Comment> findByMemberId(Long memberId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.postId = :postId ORDER BY c.createdAt ASC")
    List<Comment> findWithMemberByPostId(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.id = :id")
    Optional<Comment> findWithMemberById(@Param("id") Long id);
}