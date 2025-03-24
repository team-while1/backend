package while1.kunnect.controller.Post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import while1.kunnect.domain.Member;
import while1.kunnect.dto.post.CreatePostRequest;
import while1.kunnect.entity.Post;
import while1.kunnect.service.Post.CustomUserDetailsService;
import while1.kunnect.service.Post.PostService;
import while1.kunnect.security.CustomUserDetails;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreatePostRequest dto,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Post saved = postService.create(dto, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getPostId());
    }


    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreatePostRequest dto,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(postService.update(id, dto, userDetails.getMember()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.delete(id, userDetails.getMember());
        return ResponseEntity.noContent().build();
    }
}
