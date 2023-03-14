package np.com.oskarshrestha.loginregistration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import np.com.oskarshrestha.loginregistration.util.ForgetPasswordEmailStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPasswordResponse {
    private ForgetPasswordEmailStatus forgetPasswordEmailStatus;
}
