package while1.kunnect.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import while1.kunnect.dto.Push.PushMessageRequest;
import while1.kunnect.dto.Push.SubscriptionRequest;
import while1.kunnect.service.PushNotificationService;

@RestController
@RequestMapping("/api/push")
public class PushController {

    private final PushNotificationService pushNotificationService;

    public PushController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody SubscriptionRequest request) {
        // TODO: DB에 Subscription 정보 저장하기
        System.out.println("Subscribed: " + request);
        return ResponseEntity.ok("Subscribed");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendPush(@RequestBody PushMessageRequest request) {
        try {
            pushNotificationService.sendPushMessage(
                    request.getEndpoint(),
                    request.getUserPublicKey(),
                    request.getUserAuth(),
                    request.getPayload()
            );
            return ResponseEntity.ok("Push sent");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Push failed");
        }
    }
}