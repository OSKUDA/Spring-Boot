package np.com.oskarshrestha.loginregistration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import np.com.oskarshrestha.loginregistration.util.ResetPasswordResponseStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordResponse {
    private ResetPasswordResponseStatus resetPasswordResponseStatus;
}
