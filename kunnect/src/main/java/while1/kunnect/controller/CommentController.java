package while1.kunnect.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import while1.kunnect.domain.Comment;
import while1.kunnect.dto.comment.UpdateCommentRequest;
import while1.kunnect.service.CommentService;

import java.util.List;
import java.util.Map;

@RestController // REST API응답을 위한 컨트롤러 선언
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping
    public ResponseEntity<Comment> create(@RequestBody Comment comment) { // JSON 요청데이터를 Comment로 자동변환
        Comment saved = commentService.save(comment);
        return ResponseEntity.ok(saved);
    }

    // 댓글 단건 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getById(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> delete(
            @PathVariable Long commentId,
            @RequestParam Long memberId) {
        commentService.delete(commentId, memberId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> update(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request) {

        Comment updated = commentService.update(commentId, request.getMemberId(), request.getContent());
        return ResponseEntity.ok(updated);
    }

    // 특정 게시판 댓글 목록
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getByPost(@PathVariable Long postId) { // URL에 들어온 postID 값을 변수로 사용
        return ResponseEntity.ok(commentService.getCommentByPost(postId));
    }

    // 특정 회원 작성 댓글 목록
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Comment>> getByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(commentService.getCommentsByMember(memberId));
    }

    //댓글 필터링
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
