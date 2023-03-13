package np.com.oskarshrestha.loginregistration.service;

import np.com.oskarshrestha.loginregistration.entity.EmailVerificationToken;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.model.AuthenticationResponse;
import np.com.oskarshrestha.loginregistration.model.RegistrationResponse;
import np.com.oskarshrestha.loginregistration.model.UserAuthenticationRequest;
import np.com.oskarshrestha.loginregistration.model.UserRegisterRequest;
import np.com.oskarshrestha.loginregistration.util.EmailVerificationTokenStatus;

import java.util.Optional;

public interface UserService {
    public RegistrationResponse registerUser(UserRegisterRequest userRegisterRequest);

    public AuthenticationResponse authenticate(UserAuthenticationRequest userAuthenticationRequest);

    public void saveVerificationTokenForUser(String token, User user);

    public EmailVerificationTokenStatus verifyEmailToken(String token);
}
