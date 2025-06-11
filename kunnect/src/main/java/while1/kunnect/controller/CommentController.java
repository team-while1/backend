package while1.kunnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import while1.kunnect.domain.Comment;
import while1.kunnect.dto.comment.CommentResponse;
import while1.kunnect.dto.comment.UpdateCommentRequest;
import while1.kunnect.security.CustomUserDetails;
import while1.kunnect.service.CommentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> create(
            @RequestBody Comment comment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        comment.setMemberId(userDetails.getMember().getId()); // ← 인증된 사용자로 설정
        Comment saved = commentService.save(comment);
        return ResponseEntity.ok(saved);
    }

    // 댓글 단건 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getById(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(CommentResponse.from(comment));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.delete(commentId, userDetails.getMember().getId());
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> update(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Comment updated = commentService.update(
                commentId,
                userDetails.getMember().getId(),
                request.getContent());
        return ResponseEntity.ok(updated);
    }

    // 특정 게시글 댓글 목록 (작성자 이름 포함)
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentResponsesByPost(postId));
    }

    // 특정 회원 작성 댓글 목록
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Comment>> getByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(commentService.getCommentsByMember(memberId));
    }

    // 댓글 필터링
    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterComment(@RequestParam String content) {
        boolean isValid = !commentService.containsBannedWords(content);
        String message = isValid
                ? "사용 가능한 댓글입니다."
                : "해당 댓글은 금지된 단어를 포함하고 있습니다.";

        Map<String, Object> result = Map.of(
                "is_valid", isValid,
                "message", message
        );

        return ResponseEntity.ok(result);
    }
}