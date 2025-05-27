package while1.kunnect.dto.Push;

import lombok.Data;

@Data
public class PushMessageRequest {
    private String endpoint;
    private String userPublicKey;
    private String userAuth;
    private String payload;
}