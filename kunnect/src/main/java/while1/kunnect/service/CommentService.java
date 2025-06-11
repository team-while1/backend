package while1.kunnect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import while1.kunnect.domain.Comment;
import while1.kunnect.dto.comment.CommentResponse;
import while1.kunnect.repository.comment.BannedWordRepository;
import while1.kunnect.repository.comment.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BannedWordRepository bannedWordRepository;

    // 댓글 저장
    public Comment save(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    // 게시글 기준 댓글 목록 (작성자 포함, fetch join)
    public List<CommentResponse> getCommentResponsesByPost(Long postId) {
        return commentRepository.findWithMemberByPostId(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    // 작성자 기준 댓글 목록 (작성자 포함, fetch join)
    public List<CommentResponse> getCommentResponsesByMember(Long memberId) {
        return commentRepository.findWithMemberByMemberId(memberId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    // 댓글 단건 조회 (작성자 포함, fetch join)
    public Comment getCommentById(Long id) {
        return commentRepository.findWithMemberById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    // 댓글 수정
    @Transactional
    public Comment update(Long id, Long memberId, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        return comment;
    }

    // 댓글 삭제
    public void delete(Long id, Long memberId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }

    // 금지어 포함 여부
    public boolean containsBannedWords(String content) {
        List<String> bannedWords = bannedWordRepository.findAll()
                .stream()
                .map(bw -> bw.getWord())
                .toList();

        return bannedWords.stream().anyMatch(content::contains);
    }
}