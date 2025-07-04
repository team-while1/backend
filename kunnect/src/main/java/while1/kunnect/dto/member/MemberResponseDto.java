package while1.kunnect.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import while1.kunnect.domain.Member;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;

public record MemberResponseDto(
        @JsonProperty("member_id")
        Long id,
        String email,
        String name,
        @JsonProperty("student_num")
        String studentNum,
        String college,
        String major,
        @JsonProperty("profile_url")
        String profileUrl
) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getStudentNum().toString(),
                member.getCollege().getInfo(),
                member.getMajor().getInfo(),
                member.getProfileUrl()
        );
    }
}