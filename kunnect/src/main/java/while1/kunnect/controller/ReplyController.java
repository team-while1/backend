package while1.kunnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import while1.kunnect.domain.Reply;
import while1.kunnect.dto.reply.ReplyRequestDto;
import while1.kunnect.service.ReplyService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/replies")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<Reply> createReply(@RequestBody ReplyRequestDto dto) {
        Reply reply = replyService.createReply(dto.getCommentId(), dto.getMemberId(), dto.getContent());
        return ResponseEntity.ok(reply);
    }

    @GetMapping
    public ResponseEntity<List<Reply>> getReplies(@RequestParam Long commentId) {
        List<Reply> replies = replyService.getRepliesByComment(commentId);
        return ResponseEntity.ok(replies);
    }

    //대댓글 단일 조회
    @GetMapping("/{replyId}")
    public ResponseEntity<Reply> getReply(@PathVariable Long replyId) {
        Reply reply = replyService.getReplyById(replyId);
        return ResponseEntity.ok(reply);
    }
    //대댓글 수정
    @PutMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(
            @PathVariable Long replyId,
            @RequestBody ReplyRequestDto dto) {

        Reply updated = replyService.updateReply(replyId, dto.getMemberId(), dto.getContent());
        return ResponseEntity.ok(updated);
    }
}