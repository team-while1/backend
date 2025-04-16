package while1.kunnect.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import while1.kunnect.domain.Member;
import while1.kunnect.entity.Like;
import while1.kunnect.entity.Post;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByMemberAndPost(Member member, Post post);
    void deleteByMemberAndPost(Member member, Post post);
}

