package np.com.oskarshrestha.loginregistration.service;

import jakarta.servlet.http.HttpServletRequest;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.model.*;
import np.com.oskarshrestha.loginregistration.util.ChangeUserPasswordStatus;
import np.com.oskarshrestha.loginregistration.util.EmailVerificationTokenStatus;
import np.com.oskarshrestha.loginregistration.util.ResetPasswordResponseStatus;

import java.util.Optional;

public interface UserService {
    public RegistrationResponse registerUser(UserRegisterRequest userRegisterRequest, final HttpServletRequest request);

    public AuthenticationResponse authenticate(UserAuthenticationRequest userAuthenticationRequest);

    public void saveVerificationTokenForUser(String token, User user);

    public EmailVerificationTokenStatus verifyEmailToken(String token);

    public Optional<User> getUserByEmail(String email);

    public ChangeUserPasswordStatus changeUserPassword(String email, String oldPassword, String newPassword);

    public void saveForgetPasswordToken(String token, User user);

    public ResetPasswordResponseStatus resetUserPassword(String token,String password);

    public ResendVerifyEmailResponse resendVerificationEmail(String email,final HttpServletRequest request);

    public ForgetPasswordResponse forgetPassword(String email,final HttpServletRequest request);

    public ResendForgetPasswordEmailResponse resetForgetPasswordEmail(String email, final HttpServletRequest request);
}
