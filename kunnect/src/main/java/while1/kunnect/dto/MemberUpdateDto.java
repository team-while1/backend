package while1.kunnect.dto;
import while1.kunnect.domain.enumtype.College;
import while1.kunnect.domain.enumtype.Major;

public record MemberUpdateDto (
        String name,
        String college,
        String major
) {}