package while1.kunnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import while1.kunnect.domain.Member;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;

public record MemberDto(
        @JsonProperty("member_id")
        Long id,
        String email,
        String name,
        @JsonProperty("student_num")
        String studentNum,
        College college,
        Major major
) {
    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getStudentNum().toString(),
                member.getCollege(),
                member.getMajor()
        );
    }
}