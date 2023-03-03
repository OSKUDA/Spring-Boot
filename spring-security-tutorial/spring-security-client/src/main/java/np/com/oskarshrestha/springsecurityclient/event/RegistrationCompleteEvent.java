package np.com.oskarshrestha.springsecurityclient.event;

import lombok.Getter;
import lombok.Setter;
import np.com.oskarshrestha.springsecurityclient.entity.User;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private User user;

    // url for user to click on from the email
    private String applicationUrl;
    public RegistrationCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
