package while1.kunnect.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import while1.kunnect.dto.application.ApplicationCreateRequest;
import while1.kunnect.dto.application.ApplicationResponse;
import while1.kunnect.dto.application.ApplicationStatusUpdateRequest;
import while1.kunnect.entity.ApplicationStatus;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;
import while1.kunnect.security.CustomUserDetails;
import while1.kunnect.service.ApplicationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    // 신청하기
    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(
            @RequestBody ApplicationCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (request.postId() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        ApplicationResponse applicationResponse = applicationService.createApplication(
                request.postId(),
                userDetails.getMember().getId(),
                request.comment()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationResponse);
    }

    // 신청 취소하기
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> cancelApplication(
            @PathVariable("applicationId") Long applicationId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        applicationService.cancelApplication(applicationId, userDetails.getMember().getId());
        return ResponseEntity.noContent().build();
    }

    // 내 신청 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ApplicationResponse> applications = applicationService.getApplicationsByMember(
                userDetails.getMember().getId()
        );
        return ResponseEntity.ok(applications);
    }

    // 신청 상세 조회
    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> getApplication(
            @PathVariable("applicationId") Long applicationId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ApplicationResponse application = applicationService.getApplicationById(
                applicationId,
                userDetails.getMember().getId()
        );
        return ResponseEntity.ok(application);
    }

    // 게시글의 신청 목록 조회 (게시글 작성자 전용)
    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<ApplicationResponse>> getPostApplications(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ApplicationResponse> applications = applicationService.getApplicationsByPost(
                postId,
                userDetails.getMember().getId()
        );
        return ResponseEntity.ok(applications);
    }

    // 신청 상태 변경 (승인/거절)
    @PatchMapping("/status/{applicationId}")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable("applicationId") Long applicationId,
            @RequestBody ApplicationStatusUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (request.status() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        ApplicationResponse response = applicationService.updateApplicationStatus(
                applicationId,
                userDetails.getMember().getId(),
                request.status()
        );
        return ResponseEntity.ok(response);
    }
}