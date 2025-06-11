package while1.kunnect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import while1.kunnect.domain.Member;
import while1.kunnect.dto.application.ApplicationResponse;
import while1.kunnect.entity.Application;
import while1.kunnect.entity.ApplicationStatus;
import while1.kunnect.entity.Post;
import while1.kunnect.entity.PostUtils;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;
import while1.kunnect.repository.ApplicationRepository;
import while1.kunnect.repository.MemberRepository;
import while1.kunnect.repository.post.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 신청 생성
    public ApplicationResponse createApplication(Long postId, Long memberId, String comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        // 신청 가능 여부
        if (!PostUtils.isApplicationAvailable(post)) {
            throw new CustomException(ErrorCode.APPLICATION_NOT_AVAILABLE);
        }
        // 중복 신청 확인
        if (applicationRepository.existsByPostAndMember(post, member)) {
            throw new CustomException(ErrorCode.APPLICATION_ALREADY_EXISTS);
        }
        // 신청 생성
        Application application = Application.builder()
                .post(post)
                .member(member)
                .comment(comment)
                .status(ApplicationStatus.PENDING)
                .processedAt(LocalDateTime.now())
                .build();
        PostUtils.addApplication(post, application);
        Application saved = applicationRepository.save(application);
        return new ApplicationResponse(saved);
    }

    // 신청 취소
    public void cancelApplication(Long applicationId, Long memberId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        // 본인 확인
        if (!application.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NOT_APPLICATION_OWNER);
        }
        // 이미 승인된 신청 취소 불가
        if (application.getStatus() == ApplicationStatus.APPROVED) {
            throw new CustomException(ErrorCode.APPLICATION_ALREADY_APPROVED);
        }
        applicationRepository.delete(application);
    }

    // 회원별 신청 조회
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<Application> applications = applicationRepository.findByMember(member);
        return applications.stream()
                .map(ApplicationResponse::new)
                .collect(Collectors.toList());
    }

    // 신청 상세 조회
    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationById(Long applicationId, Long memberId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        // 본인 또는 게시글 작성자만 조회 가능
        if (!application.getMember().getId().equals(memberId) &&
                !application.getPost().getWriter().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NOT_APPLICATION_OWNER);
        }
        // 게시글 정보, 신청자 정보, 신청 상태 등을 알 수 있다
        return new ApplicationResponse(application);
    }

    // 게시글별 신청 조회
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsByPost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // 게시글 작성자 확인
        if (!post.getWriter().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NOT_POST_OWNER);
        }
        List<Application> applications = applicationRepository.findByPost(post);
        return applications.stream()
                .map(ApplicationResponse::new)
                .collect(Collectors.toList());
    }

    // 신청 상태 변경
    public ApplicationResponse updateApplicationStatus(Long applicationId, Long memberId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        // 게시글 작성자 확인
        if (!application.getPost().getWriter().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NOT_POST_OWNER);
        }
        // 이미 처리된 신청 확인
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        // 상태 변경 - Application 엔티티의 메서드 활용
        if (newStatus == ApplicationStatus.APPROVED) {
            application.approve();
            application.setProcessedBy(memberRepository.findById(memberId).orElse(null));
        } else if (newStatus == ApplicationStatus.REJECTED) {
            application.reject("신청이 거절되었습니다.");
            application.setProcessedBy(memberRepository.findById(memberId).orElse(null));
        } else {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        return new ApplicationResponse(application);
    }
}