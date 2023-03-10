package np.com.oskarshrestha.loginregistration.controller;

import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.model.RegistrationResponse;
import np.com.oskarshrestha.loginregistration.model.UserRegisterRequest;
import np.com.oskarshrestha.loginregistration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<RegistrationResponse> registerUser(@RequestBody UserRegisterRequest userRegisterRequest){
        User user = userService.registerUser(userRegisterRequest);
        RegistrationResponse registrationResponse = new RegistrationResponse("SUCCESS");
        return ResponseEntity.ok(registrationResponse);
    }
}
