package while1.kunnect.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import while1.kunnect.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    // PostRepository.java

    // PostRepository.java
    @Query("SELECT p FROM Post p JOIN FETCH p.writer WHERE p.postId = :id")
    Optional<Post> findByIdWithWriter(@Param("id") Long id);

}

