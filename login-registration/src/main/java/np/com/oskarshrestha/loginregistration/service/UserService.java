package np.com.oskarshrestha.loginregistration.service;

import np.com.oskarshrestha.loginregistration.model.AuthenticationResponse;
import np.com.oskarshrestha.loginregistration.model.RegistrationResponse;
import np.com.oskarshrestha.loginregistration.model.UserAuthenticationRequest;
import np.com.oskarshrestha.loginregistration.model.UserRegisterRequest;

public interface UserService {
    public RegistrationResponse registerUser(UserRegisterRequest userRegisterRequest);

    public AuthenticationResponse authenticate(UserAuthenticationRequest userAuthenticationRequest);
}
