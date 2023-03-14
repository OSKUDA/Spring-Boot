package np.com.oskarshrestha.loginregistration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import np.com.oskarshrestha.loginregistration.util.ResendVerifyEmailStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResendVerifyEmailResponse {
    private ResendVerifyEmailStatus resendVerifyEmailStatus;
}
