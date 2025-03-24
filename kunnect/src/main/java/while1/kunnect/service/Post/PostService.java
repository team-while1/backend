package while1.kunnect.service.Post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import while1.kunnect.domain.Member;
import while1.kunnect.dto.post.CreatePostRequest;
import while1.kunnect.dto.post.PostResponse;
import while1.kunnect.entity.Post;
import while1.kunnect.repository.post.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post create(CreatePostRequest dto, Member writer) {
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(writer)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .categoryId(dto.getCategoryId())
                .archived(dto.getArchived())
                .totalSlots(dto.getTotalSlots())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return postRepository.save(post);
    }

    public List<PostResponse> getAll() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public PostResponse getOne(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글 없음"));
        return toDto(post);
    }

    public Post update(Long id, CreatePostRequest dto, Member writer) {
        Post post = getOneEntity(id);
        if (!post.getWriter().getId().equals(writer.getId())) {
            throw new RuntimeException("본인 글만 수정 가능");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setStartDate(dto.getStartDate());
        post.setEndDate(dto.getEndDate());
        post.setCategoryId(dto.getCategoryId());
        post.setTotalSlots(dto.getTotalSlots());
        post.setArchived(dto.getArchived());
        post.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }

    public void delete(Long id, Member writer) {
        Post post = getOneEntity(id);
        if (!post.getWriter().getId().equals(writer.getId())) {
            throw new RuntimeException("본인 글만 삭제 가능");
        }
        postRepository.delete(post);
    }

    private PostResponse toDto(Post post) {
        return PostResponse.builder()
                .id(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .writerName(post.getWriter().getName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .categoryId(post.getCategoryId())
                .build();
    }

    private Post getOneEntity(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글 없음"));
    }
}


