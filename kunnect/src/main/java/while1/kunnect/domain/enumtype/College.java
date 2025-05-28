package while1.kunnect.domain.enumtype;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import while1.kunnect.exception.CustomException;
import while1.kunnect.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum College {
    CONVERGENCE_TECHNOLOGY("융합기술대학"),
    ENGINEERING("공과대학"),
    HUMANITIES("인문대학"),
    SOCIAL_SCIENCES("사회과학대학"),
    HEALTH_LIFE_SCIENCES("보건생명대학"),
    RAILROAD_SCIENCES("철도대학"),
    FUTURE_CONVERGENCE("미래융합대학"),
    LIBERAL_ARTS_SCIENCES("교양학부"),
    LIBERAL_STUDIES("자유전공학부"),
    CREATIVE_CONVERGENCE("창의융합학부");

    private final String info;

    public static College fromKoreanName(String koreanName) {
        return Arrays.stream(College.values())
                .filter(college -> college.info.equals(koreanName))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_COLLEGE));
    }
}
