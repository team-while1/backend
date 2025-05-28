package while1.kunnect.dto.Push;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private String endpoint;
    private String userPublicKey;
    private String userAuth;
}