package while1.kunnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import while1.kunnect.domain.Member;
import while1.kunnect.entity.Application;
import while1.kunnect.entity.ApplicationStatus;
import while1.kunnect.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByMember(Member member);

    List<Application> findByPost(Post post);

    Optional<Application> findByPostAndMember(Post post, Member member);

    boolean existsByPostAndMember(Post post, Member member);

    List<Application> findByPostAndStatus(Post post, ApplicationStatus status);

    int countByPostAndStatus(Post post, ApplicationStatus status);
}