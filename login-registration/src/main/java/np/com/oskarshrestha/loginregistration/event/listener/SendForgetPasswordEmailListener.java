package np.com.oskarshrestha.loginregistration.event.listener;

import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.event.SendForgetPasswordEmailEvent;
import np.com.oskarshrestha.loginregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;
import java.util.UUID;

@Component
public class SendForgetPasswordEmailListener implements ApplicationListener<SendForgetPasswordEmailEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(SendForgetPasswordEmailEvent event) {
        // create the verification token for the user with link
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        try {
            userService.saveForgetPasswordToken(token, user);
            // send mail to user
            String url = event.getApplicationUrl() + "/api/v1/auth/resetPassword?token=" + token;

            System.out.println("Reset password: " + url);
        } catch (Exception e) {
            System.out.println("Found null value: "+e.getMessage());
            System.out.println("User: "+user);
            System.out.println("Token: "+token);
        }


    }
}
