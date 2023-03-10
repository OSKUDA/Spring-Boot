package np.com.oskarshrestha.loginregistration.controller;

import np.com.oskarshrestha.loginregistration.model.AuthenticationResponse;
import np.com.oskarshrestha.loginregistration.model.RegistrationResponse;
import np.com.oskarshrestha.loginregistration.model.UserAuthenticationRequest;
import np.com.oskarshrestha.loginregistration.model.UserRegisterRequest;
import np.com.oskarshrestha.loginregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        return ResponseEntity.ok(userService.registerUser(userRegisterRequest));
    }

    @PostMapping("/api/v1/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody UserAuthenticationRequest userAuthenticationRequest) {
        return ResponseEntity.ok(userService.authenticate(userAuthenticationRequest));
    }
}
