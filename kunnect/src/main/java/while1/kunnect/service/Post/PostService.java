package while1.kunnect.service.Post;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import while1.kunnect.domain.Member;
import while1.kunnect.dto.post.CreatePostRequest;
import while1.kunnect.dto.post.PostResponse;
import while1.kunnect.entity.*;
import while1.kunnect.repository.post.LikeRepository;
import while1.kunnect.repository.post.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public Post create(CreatePostRequest dto, Member writer) {
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(writer)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .categoryId(dto.getCategoryId())
             // .archived(dto.getArchived()) // 변수 변경 -> status
                .status(PostStatus.OPEN)
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

    @Transactional
    public PostResponse getOne(Long id) {
        Post post = postRepository.findByIdWithWriter(id)
                .orElseThrow(() -> new RuntimeException("글 없음"));

        post.setViews(post.getViews() + 1);
        postRepository.save(post);

        return toDto(post);  // toDto 안에서 writerName 잘 작동함
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
        //post.setArchived(dto.getArchived()); // 변수 변경 -> status
        //post.setStatus(dto.getStatus());     // FIXME) 우선 주석 처리 함.
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
                .memberId(post.getWriter().getId())
                .writerName(post.getWriter().getName())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .categoryId(post.getCategoryId())
                .views(post.getViews())
                .likes(post.getLikes())
                .totalSlots(post.getTotalSlots())
                .build();
    }


    private Post getOneEntity(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("글 없음"));
    }

    @Transactional
    public void toggleLike(Long postId, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("글 없음"));

        boolean alreadyLiked = likeRepository.existsByMemberAndPost(member, post);

        if (alreadyLiked) {
            likeRepository.deleteByMemberAndPost(member, post);
            post.setLikes(post.getLikes() - 1);
        } else {
            Like like = Like.builder()
                    .member(member)
                    .post(post)
                    .build();
            likeRepository.save(like);
            post.setLikes(post.getLikes() + 1);
        }

        postRepository.save(post);
    }
}


