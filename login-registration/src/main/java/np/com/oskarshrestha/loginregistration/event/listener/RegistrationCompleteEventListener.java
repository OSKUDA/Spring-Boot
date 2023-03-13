package np.com.oskarshrestha.loginregistration.event.listener;

import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.event.RegistrationCompleteEvent;
import np.com.oskarshrestha.loginregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // create the verification token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, user);

        // send mail to user
        String url = event.getApplicationUrl()
                + "/api/v1/auth/verifyEmail?token="
                + token;

        System.out.println("EMAIL VERIFY LINK: "+url);
    }
}
