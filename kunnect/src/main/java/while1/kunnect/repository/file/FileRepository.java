package while1.kunnect.repository.file;

import while1.kunnect.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByPostId(Long postId);
}