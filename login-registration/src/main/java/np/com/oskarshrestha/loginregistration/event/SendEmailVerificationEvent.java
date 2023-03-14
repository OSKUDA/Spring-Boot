package np.com.oskarshrestha.loginregistration.event;

import lombok.*;
import np.com.oskarshrestha.loginregistration.entity.User;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SendEmailVerificationEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public SendEmailVerificationEvent(User user, String applicationUrl){
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }

}
