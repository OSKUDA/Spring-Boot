package np.com.oskarshrestha.loginregistration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import np.com.oskarshrestha.loginregistration.util.ResendForgetPasswordEmailStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResendForgetPasswordEmailResponse {
    private ResendForgetPasswordEmailStatus resendForgetPasswordEmailStatus;
}
