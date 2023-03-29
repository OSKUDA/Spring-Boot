package np.com.oskarshrestha.loginregistration.controller;

import jakarta.servlet.http.HttpServletRequest;
import np.com.oskarshrestha.loginregistration.model.*;
import np.com.oskarshrestha.loginregistration.service.UserService;
import np.com.oskarshrestha.loginregistration.util.ChangeUserPasswordStatus;
import np.com.oskarshrestha.loginregistration.util.EmailVerificationTokenStatus;
import np.com.oskarshrestha.loginregistration.util.ResetPasswordResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @Autowired
    private UserService userService;


    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<RegistrationResponse> registerUser(
            @RequestBody UserRegisterRequest userRegisterRequest,
            final HttpServletRequest request
    ) {
        RegistrationResponse response = userService.registerUser(userRegisterRequest, request);
        if(response.isRegistrationSuccess()){
            return ResponseEntity.ok(response);

        }else{
            return ResponseEntity.badRequest().body(response);
        }
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
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @GetMapping("/api/v1/auth/resendEmailVerification")
    public ResponseEntity<ResendVerifyEmailResponse> resendVerifyEmail(
            @RequestParam("email") String email,
            final HttpServletRequest request
    ) {
        return ResponseEntity.ok(userService.resendVerificationEmail(email, request));
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
        return ResponseEntity.ok(userService.forgetPassword(email, request));
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
    public ResponseEntity<ResendForgetPasswordEmailResponse> resendForgetPasswordEmail(
            @RequestParam("email") String email,
            final HttpServletRequest request
    ) {
        return ResponseEntity.ok(userService.resendForgetPasswordEmail(email, request));
    }

}
