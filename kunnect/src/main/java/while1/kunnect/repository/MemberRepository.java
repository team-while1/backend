package while1.kunnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import while1.kunnect.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
