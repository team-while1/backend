package while1.kunnect.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import while1.kunnect.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByName(String name);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
