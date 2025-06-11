package while1.kunnect.config;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import while1.kunnect.entity.Post;
import while1.kunnect.repository.post.PostRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FixNullAppliedCountRunner {

    private final PostRepository postRepository;

    @PostConstruct
    @Transactional
    public void fixNullAppliedCounts() {
        List<Post> posts = postRepository.findAll();
        int fixed = 0;

        for (Post post : posts) {
            if (post.getAppliedCount() == null) {
                post.setAppliedCount(0);
                fixed++;
            }
        }

        if (fixed > 0) {
            System.out.println("[INFO] appliedCount null → 0 초기화 완료: " + fixed + "건");
        } else {
            System.out.println("[INFO] 적용할 appliedCount 없음");
        }
    }
}
