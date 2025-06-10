package while1.kunnect.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import while1.kunnect.domain.Comment;
import while1.kunnect.domain.Member;
import while1.kunnect.domain.Reply;
import while1.kunnect.repository.ReplyRepository;
import while1.kunnect.repository.comment.CommentRepository;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final CommentRepository commentRepository;

    // 대댓글 작성
    @Transactional
    public Reply createReply(Long commentId, Long memberId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        Reply reply = Reply.builder()
                .comment(comment)
                .writer(Member.builder().id(memberId).build())
                .content(content)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return replyRepository.save(reply);
    }

    // 특정 댓글에 달린 대댓글 목록 조회(오래된순)
    @Transactional(readOnly = true)
    public List<Reply> getRepliesByComment(Long commentId){
        return replyRepository.findByCommentIdOrderByCreatedAtAsc(commentId);
    }

    //단일 조회
    public Reply getReplyById(Long id) {
        return replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("대댓글이 존재하지 않습니다."));
    }


    //삭제
    public void deleteReply(Long id, Long memberId) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("대댓글이 존재하지 않습니다."));

        if (!reply.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        replyRepository.delete(reply);
    }

    //수정
    @Transactional
    public Reply updateReply(Long id, Long memberId, String content) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("대댓글이 존재하지 않습니다."));

        if (!reply.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        reply.setContent(content);
        reply.setUpdatedAt(LocalDateTime.now());
        return reply;
    }
}
