package np.com.oskarshrestha.loginregistration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import np.com.oskarshrestha.loginregistration.util.EmailVerificationTokenStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailResponse {
    private EmailVerificationTokenStatus emailVerificationTokenStatus;
}
