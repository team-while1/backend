package while1.kunnect.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import while1.kunnect.domain.Member;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;
import while1.kunnect.domain.enumtype.Role;
import while1.kunnect.dto.MemberUpdateDto;
import while1.kunnect.dto.UpdatePasswordRequest;
import while1.kunnect.dto.sign.AddUserRequest;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;
import while1.kunnect.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findMember(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = getUserEmailFromAuthentication();
        return memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Long save(AddUserRequest dto) {
        return memberRepository.save(Member.builder()
                .email(dto.email())
                .name(dto.name())
                .password(bCryptPasswordEncoder.encode(dto.password()))
                .major(dto.major())
                .college(dto.college())
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
                        College college = College.valueOf(newText);
                        member.setCollege(college);
                    } catch (IllegalArgumentException e) {
                        throw new CustomException(ErrorCode.INVALID_COLLEGE);
                    }
                });
        Optional.ofNullable(request.major())
                .ifPresent(newText -> {
                    try {
                        Major major = Major.valueOf(newText);
                        member.setMajor(major);
                    } catch (IllegalArgumentException e) {
                        throw new CustomException(ErrorCode.INVALID_MAJOR);
                    }
                });
        return member;
    }

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
}
