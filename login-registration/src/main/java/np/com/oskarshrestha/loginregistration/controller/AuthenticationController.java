package np.com.oskarshrestha.loginregistration.controller;

import jakarta.servlet.http.HttpServletRequest;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.event.RegistrationCompleteEvent;
import np.com.oskarshrestha.loginregistration.model.*;
import np.com.oskarshrestha.loginregistration.service.UserService;
import np.com.oskarshrestha.loginregistration.util.EmailVerificationTokenStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<RegistrationResponse> registerUser(
            @RequestBody UserRegisterRequest userRegisterRequest,
            final HttpServletRequest request
            ) {
        RegistrationResponse registrationResponse = userService.registerUser(userRegisterRequest);
        User user = registrationResponse.getUser();
        System.out.println(applicationEventPublisher.toString());
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(
                user,
                generateApplicationUrl(request)
        ));
        System.out.println("Event sent");
        return ResponseEntity.ok(registrationResponse);
    }

    @GetMapping("/api/v1/auth/verifyEmail")
    public ResponseEntity<VerifyRegistrationResponse> verifyRegistration(@RequestParam("token") String token){
        EmailVerificationTokenStatus status = userService.verifyEmailToken(token);
        switch (status){
            case VALID -> {
                return ResponseEntity.ok(VerifyRegistrationResponse
                        .builder()
                        .emailVerificationTokenStatus(EmailVerificationTokenStatus.VALID)
                        .build()
                );
            }
            case INVALID -> {
                return ResponseEntity.ok(VerifyRegistrationResponse
                        .builder()
                        .emailVerificationTokenStatus(EmailVerificationTokenStatus.INVALID)
                        .build()
                );
            }
            case EXPIRED -> {
                return ResponseEntity.ok(VerifyRegistrationResponse
                        .builder()
                        .emailVerificationTokenStatus(EmailVerificationTokenStatus.EXPIRED)
                        .build()
                );
            }
            default -> {
                return ResponseEntity.status(404).build();
            }
        }
    }


    @PostMapping("/api/v1/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody UserAuthenticationRequest userAuthenticationRequest) {
        return ResponseEntity.ok(userService.authenticate(userAuthenticationRequest));
    }

    private String generateApplicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }
}
