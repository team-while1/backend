package while1.kunnect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import while1.kunnect.domain.Comment;
import while1.kunnect.repository.comment.BannedWordRepository;
import while1.kunnect.repository.comment.CommentRepository;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    //댓글 저장
    public Comment save(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
    // 게시글 기준으로 댓글 목록 조회
    public List<Comment> getCommentByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }

    //댓글 단일 조회
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    // 작성자 기준으로 댓글 목록 조회
    public List<Comment> getCommentsByMember(Long memberId) {
        return commentRepository.findByMemberId(memberId);
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

    private final BannedWordRepository bannedWordRepository;

    //댓글 필터링(db에 금지어 추가해야함)
    public boolean containsBannedWords(String content) {
        List<String> bannedWords = bannedWordRepository.findAll()
                .stream()
                .map(bw -> bw.getWord())
                .toList();

        return bannedWords.stream().anyMatch(content::contains);
    }

}
