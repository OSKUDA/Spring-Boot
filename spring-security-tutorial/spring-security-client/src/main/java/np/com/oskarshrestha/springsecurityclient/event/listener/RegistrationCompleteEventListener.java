package np.com.oskarshrestha.springsecurityclient.event.listener;

import lombok.extern.slf4j.Slf4j;
import np.com.oskarshrestha.springsecurityclient.entity.User;
import np.com.oskarshrestha.springsecurityclient.event.RegistrationCompleteEvent;
import np.com.oskarshrestha.springsecurityclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create the verification token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);
        // Send mail to user
        String url = event.getApplicationUrl()
                + "/verifyRegistration?token="
                +token;

        //TODO: send verification email method here.
        // We are mocking the email by printing it out in console.
        log.info("Click the link to verify your account: {}",url);
    }
}
