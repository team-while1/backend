package while1.kunnect.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글를 찾을 수 없습니다."),
    REFRESHTOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "refreshToken를 찾을 수 없습니다."),

//    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    NAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT, "좋아요를 이미 누르셨습니다."),
    ALREADY_UNLIKED(HttpStatus.BAD_REQUEST, "싫어요를 이미 누르셨습니다."),

    LIKED_NOT_FOUND(HttpStatus.BAD_REQUEST, "좋아요가 등록된 상태가 아닙니다."),
    UNLIKED_NOT_FOUND(HttpStatus.BAD_REQUEST, "싫어요가 등록된 상태가 아닙니다."),
    UNLIKED_LIKED_NOT_FOUND(HttpStatus.BAD_REQUEST, "좋아요/싫어요가 등록된 상태가 아닙니다."),


    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 잘못되었습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. 필수 값을 입력하세요."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "새 비밀번호와 확인 비밀번호가 일치하지 않습니다."),
    INVALID_GOOGLE_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 구글 토큰입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
