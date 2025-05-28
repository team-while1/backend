package while1.kunnect.service;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

@Service
public class PushNotificationService {

    @Value("${vapid.public-key}")
    private String publicKey;

    @Value("${vapid.private-key}")
    private String privateKey;

    public void sendPushMessage(String endpoint, String userPublicKey, String userAuth, String payload)
            throws GeneralSecurityException, IOException, JoseException, ExecutionException, InterruptedException {

        PushService pushService = new PushService()
                .setPublicKey(Utils.loadPublicKey(publicKey))
                .setPrivateKey(Utils.loadPrivateKey(privateKey))
                .setSubject("mailto:your-email@example.com"); // 이메일은 식별용

        Notification notification = new Notification(endpoint, userPublicKey, userAuth, payload);

        HttpResponse response = pushService.send(notification);

        System.out.println("Push Response: " + response.getStatusLine().getStatusCode());
    }
}