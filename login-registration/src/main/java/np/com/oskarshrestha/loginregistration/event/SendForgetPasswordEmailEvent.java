package np.com.oskarshrestha.loginregistration.event;

import lombok.Getter;
import lombok.Setter;
import np.com.oskarshrestha.loginregistration.entity.User;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SendForgetPasswordEmailEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    public SendForgetPasswordEmailEvent(User user, String applicationUrl){
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }

}
