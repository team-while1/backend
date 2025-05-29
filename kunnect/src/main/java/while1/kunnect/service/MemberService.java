package while1.kunnect.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import while1.kunnect.domain.Member;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;
import while1.kunnect.domain.enumtype.Role;
import while1.kunnect.dto.member.MemberUpdateDto;
import while1.kunnect.dto.member.ProfileUpdateDto;
import while1.kunnect.dto.member.UpdatePasswordRequest;
import while1.kunnect.dto.member.AddUserRequest;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;
import while1.kunnect.repository.MemberRepository;
import while1.kunnect.security.CustomUserDetails;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private static final String UPLOAD_PRE = "/var/www";
    private static final String UPLOAD_DIR = "/images/profile";
    private static final String BASIC_PIC = UPLOAD_DIR + "/anonymous.png";
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /*
    public Member findMember(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = getUserEmailFromAuthentication();
        return memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
    */
    public Member findMember(HttpServletRequest request, HttpServletResponse response) {
        // 1. Authentication 객체 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        /*
        System.out.println("🔍 Authentication: " + auth);
        System.out.println("🔍 Authentication class: " + (auth != null ? auth.getClass().getName() : "null"));
        System.out.println("🔍 Is authenticated: " + (auth != null ? auth.isAuthenticated() : "false"));
        System.out.println("🔍 Principal: " + (auth != null ? auth.getPrincipal() : "null"));
        System.out.println("🔍 Principal class: " + (auth != null && auth.getPrincipal() != null ? auth.getPrincipal().getClass().getName() : "null"));
        */

        // 2. CustomUserDetails에서 직접 Member 가져오기
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            Member member = userDetails.getMember();
        //    System.out.println("🔍 Member from CustomUserDetails: " + member.getEmail());
            return member;
        }

        // 3. 대안: getUserEmailFromAuthentication() 사용
        String userEmail = getUserEmailFromAuthentication();
        // System.out.println("🔍 Extracted email: '" + userEmail + "'");

        if (userEmail == null || userEmail.trim().isEmpty()) {
            System.out.println("❌ Email is null or empty!");
            throw new CustomException(ErrorCode.INVALID_MAJOR);
        }
        // 4. DB에서 조회
        // System.out.println("🔍 Searching for email in DB: " + userEmail);
        Optional<Member> memberOpt = memberRepository.findByEmail(userEmail);
        // System.out.println("🔍 Member found: " + memberOpt.isPresent());

        return memberOpt.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Long save(AddUserRequest dto) {
        return memberRepository.save(Member.builder()
                .email(dto.email())
                .name(dto.name())
                .password(bCryptPasswordEncoder.encode(dto.password()))
                .major(Major.fromKoreanName(dto.major()))
                .college(College.fromKoreanName(dto.college()))
                .studentNum(Long.valueOf(dto.studentNum()))
                .role(Role.USER)
                .build())
        .getId();
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member getMemberByName(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public boolean checkDuplicationForEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean checkDuplicationForName(String name) {
        return memberRepository.existsByName(name);
    }

    @Transactional
    public Map<String, String> saveAndGetTokens(Member member, String accessToken, String refreshToken) {
        member.setRefreshToken(refreshToken);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = getUserEmailFromAuthentication();
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.setRefreshToken(null);
        new SecurityContextLogoutHandler().logout(request, response, auth);
    }


    public void deleteById(HttpServletRequest request, HttpServletResponse response) {
        String userEmail = getUserEmailFromAuthentication();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }

    @Transactional
    public Member updateMember(MemberUpdateDto request) {
        String userEmail = getUserEmailFromAuthentication();
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Optional.ofNullable(request.name())
                .ifPresent(name -> {
                    if (memberRepository.existsByName(name)) {
                        throw new CustomException(ErrorCode.NAME_ALREADY_EXISTS);
                    }
                    member.setName(name);
                });
        Optional.ofNullable(request.college())
                .ifPresent(newText -> {
                    try {
                        College college = College.fromKoreanName(newText);
                        System.out.println(college);
                        member.setCollege(college);
                    } catch (IllegalArgumentException e) {
                        throw new CustomException(ErrorCode.INVALID_COLLEGE);
                    }
                });
        Optional.ofNullable(request.major())
                .ifPresent(newText -> {
                    try {
                        Major major = Major.fromKoreanName(newText);
                        System.out.println(major);
                        member.setMajor(major);
                    } catch (IllegalArgumentException e) {
                        throw new CustomException(ErrorCode.INVALID_MAJOR);
                    }
                });
        return member;
    }

    public String getUserEmailFromAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // System.out.println("🔍 getUserEmailFromAuthentication 시작");
        // System.out.println("🔍 Authentication: " + auth);

        if (auth == null || !auth.isAuthenticated()) {
            // System.out.println("❌ Authentication is null or not authenticated");
            return null;
        }

        Object principal = auth.getPrincipal();
        // System.out.println("🔍 Principal: " + principal);
        // System.out.println("🔍 Principal type: " + principal.getClass().getName());

        // CustomUserDetails인 경우
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            String username = userDetails.getUsername(); // 이게 email
            // System.out.println("🔍 CustomUserDetails username (email): " + username);
            return username;
        }

        // 문자열인 경우 (혹시 다른 경우)
        if (principal instanceof String) {
            // System.out.println("🔍 String principal: " + principal);
            return (String) principal;
        }

        // UserDetails 구현체인 경우
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            // System.out.println("🔍 UserDetails username: " + username);
            return username;
        }

        // System.out.println("❌ Unknown principal type: " + principal.getClass().getName());
        return null;
    }
    /*
    public String getUserEmailFromAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return Optional.of(auth.getPrincipal())
                .filter(UserDetails.class::isInstance)
                .map(principal -> ((UserDetails) principal).getUsername())
                .orElseGet(() -> {
                    if (auth.getPrincipal() instanceof String) {
                        return (String) auth.getPrincipal();
                    }
                    throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                });
    }
    */

    public Member findEmail(String stringOfStudentNum) {
        Long studentNum = Long.valueOf(stringOfStudentNum);
        return memberRepository.findByStudentNum(studentNum)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void updatePassword(UpdatePasswordRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!member.getName().equals(request.name())){
            throw new CustomException(ErrorCode.INVALID_NAME);
        }
        if (!member.getStudentNum().equals(Long.valueOf(request.studentNum()))) {
            throw new CustomException(ErrorCode.INVALID_STUDENT_NUM);
        }
        member.setPassword(bCryptPasswordEncoder.encode(request.password()));
    }

    public Member updateProfile(ProfileUpdateDto request) {
        if (request.image() == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        Member member = memberRepository.findByEmail(getUserEmailFromAuthentication())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        try {
            Files.createDirectories(Paths.get(UPLOAD_PRE + UPLOAD_DIR));
            String savedPath = saveNewImage(request.image());
            deleteExistingImage(member.getProfileUrl());
            member.setProfileUrl(savedPath);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.ERROR_IMAGE_THING);
        }
        return member;
    }


    private String saveNewImage(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".png";
        String fileName = UUID.randomUUID() + extension;
        Path filePath = Paths.get(UPLOAD_PRE + UPLOAD_DIR + "/" + fileName);
        String result = UPLOAD_DIR + "/" + fileName;
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return result;
    }

    private void deleteExistingImage(String currentPhotoUrl) {
        try {
            Files.deleteIfExists(Paths.get(currentPhotoUrl));
        } catch (IOException e) {
            log.warn("기존 이미지 삭제 중 오류 발생: {}", e.getMessage());
        }
    }

    public Member updateBasicProfile() {
        Member member = memberRepository.findByEmail(getUserEmailFromAuthentication())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        deleteExistingImage(member.getProfileUrl());
        member.setProfileUrl(BASIC_PIC);
        return member;
    }
}
