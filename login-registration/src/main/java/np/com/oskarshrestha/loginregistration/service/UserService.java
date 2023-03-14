package np.com.oskarshrestha.loginregistration.service;

import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.model.*;
import np.com.oskarshrestha.loginregistration.util.ChangeUserPasswordStatus;
import np.com.oskarshrestha.loginregistration.util.EmailVerificationTokenStatus;
import np.com.oskarshrestha.loginregistration.util.ResetPasswordResponseStatus;

import java.util.Optional;

public interface UserService {
    public RegisterUserResponse registerUser(UserRegisterRequest userRegisterRequest);

    public AuthenticationResponse authenticate(UserAuthenticationRequest userAuthenticationRequest);

    public void saveVerificationTokenForUser(String token, User user);

    public EmailVerificationTokenStatus verifyEmailToken(String token);

    public Optional<User> getUserByEmail(String email);

    public ChangeUserPasswordStatus changeUserPassword(String email, String oldPassword, String newPassword);

    public void saveForgetPasswordToken(String token, User user);

    public ResetPasswordResponseStatus resetUserPassword(String token,String password);
}
