package while1.kunnect.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import while1.kunnect.domain.BannedWord;


public interface BannedWordRepository extends JpaRepository<BannedWord, Long> {
}
