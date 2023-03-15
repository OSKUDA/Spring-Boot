package np.com.oskarshrestha.loginregistration.controller;

import jakarta.servlet.http.HttpServletRequest;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.event.SendEmailVerificationEvent;
import np.com.oskarshrestha.loginregistration.event.SendForgetPasswordEmailEvent;
import np.com.oskarshrestha.loginregistration.model.*;
import np.com.oskarshrestha.loginregistration.service.UserService;
import np.com.oskarshrestha.loginregistration.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        RegisterUserResponse registerUserResponse = userService.registerUser(userRegisterRequest);

        // check if the user has already registered
        if (registerUserResponse.isExistingUser()) {
            return ResponseEntity.ok(RegistrationResponse
                    .builder()
                    .existingUser(true)
                    .build()
            );
        }

        // continue creating user
        User user = registerUserResponse.getUser();
        applicationEventPublisher.publishEvent(new SendEmailVerificationEvent(
                user,
                generateApplicationUrl(request)
        ));

        return ResponseEntity.ok(RegistrationResponse
                .builder()
                .registrationSuccess(true)
                .existingUser(false)
                .build()
        );

    }

    @GetMapping("/api/v1/auth/verifyEmail")
    public ResponseEntity<VerifyEmailResponse> verifyEmail(
            @RequestParam("token") String token
    ) {
        EmailVerificationTokenStatus status = userService.verifyEmailToken(token);
        switch (status) {
            case VALID -> {
                return ResponseEntity.ok(VerifyEmailResponse.builder().emailVerificationTokenStatus(EmailVerificationTokenStatus.VALID).build());
            }
            case INVALID -> {
                return ResponseEntity.ok(VerifyEmailResponse.builder().emailVerificationTokenStatus(EmailVerificationTokenStatus.INVALID).build());
            }
            case EXPIRED -> {
                return ResponseEntity.ok(VerifyEmailResponse.builder().emailVerificationTokenStatus(EmailVerificationTokenStatus.EXPIRED).build());
            }
            default -> {
                return ResponseEntity.status(404).build();
            }
        }
    }

    @GetMapping("/api/v1/auth/resendEmailVerification")
    public ResponseEntity<ResendVerifyEmailResponse> resendVerifyEmail(
            @RequestParam("email") String email,
            final HttpServletRequest request
    ) {

        Optional<User> user = userService.getUserByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.ok(ResendVerifyEmailResponse.builder().resendVerifyEmailStatus(ResendVerifyEmailStatus.EMAIL_NOT_REGISTERED).build());
        }

        applicationEventPublisher.publishEvent(new SendEmailVerificationEvent(
                        user.get(),
                        generateApplicationUrl(request)
                )
        );
        return ResponseEntity.ok(ResendVerifyEmailResponse.builder().resendVerifyEmailStatus(ResendVerifyEmailStatus.SUCCESS).build());
    }


    @PostMapping("/api/v1/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @RequestBody UserAuthenticationRequest userAuthenticationRequest
    ) {
        return ResponseEntity.ok(userService.authenticate(userAuthenticationRequest));
    }

    @PostMapping("/api/v1/auth/changePassword")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        ChangeUserPasswordStatus changeUserPasswordStatus = userService.changeUserPassword(
                changePasswordRequest.getEmail(),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
        return ResponseEntity.ok(ChangePasswordResponse
                .builder()
                .changeUserPasswordStatus(changeUserPasswordStatus)
                .build()
        );
    }

    @GetMapping("/api/v1/auth/forgetPassword")
    public ResponseEntity<ForgetPasswordResponse> forgetPassword(
            @RequestParam("email") String email,
            final HttpServletRequest request
    ) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.ok(ForgetPasswordResponse
                    .builder()
                    .forgetPasswordEmailStatus(ForgetPasswordEmailStatus.EMAIL_NOT_FOUND)
                    .build()
            );
        }

        applicationEventPublisher.publishEvent(new SendForgetPasswordEmailEvent(
                user.get(),
                generateApplicationUrl(request)
        ));

        return ResponseEntity.ok(ForgetPasswordResponse
                .builder()
                .forgetPasswordEmailStatus(ForgetPasswordEmailStatus.SENT)
                .build()
        );
    }

    @PostMapping("/api/v1/auth/resetPassword")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @RequestParam("token") String token,
            @RequestBody ResetPasswordRequest resetPasswordRequest
    ) {
        ResetPasswordResponseStatus resetPasswordResponseStatus = userService.resetUserPassword(token, resetPasswordRequest.getPassword());
        return ResponseEntity.ok(ResetPasswordResponse
                .builder()
                .resetPasswordResponseStatus(resetPasswordResponseStatus)
                .build()
        );
    }

    @GetMapping("/api/v1/auth/resendForgetPasswordEmail")
    public ResponseEntity<ResendForgetPasswordEmailResponse> resetForgetPasswordEmail(
            @RequestParam("email") String email,
            final HttpServletRequest request
    ) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.ok(ResendForgetPasswordEmailResponse
                    .builder()
                    .resendForgetPasswordEmailStatus(
                            ResendForgetPasswordEmailStatus.EMAIL_NOT_REGISTERED
                    )
                    .build()
            );
        }

        applicationEventPublisher.publishEvent(new SendForgetPasswordEmailEvent(
                user.get(),
                generateApplicationUrl(request)
        ));
        return ResponseEntity.ok(ResendForgetPasswordEmailResponse
                .builder()
                .resendForgetPasswordEmailStatus(ResendForgetPasswordEmailStatus.SENT)
                .build()
        );
    }

    private String generateApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
