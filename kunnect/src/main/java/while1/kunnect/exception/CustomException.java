package while1.kunnect.exception;
import org.springframework.http.HttpStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public CustomException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }
}
