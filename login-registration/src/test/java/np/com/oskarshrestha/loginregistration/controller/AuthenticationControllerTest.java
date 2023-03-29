package np.com.oskarshrestha.loginregistration.controller;

import np.com.oskarshrestha.loginregistration.model.*;
import np.com.oskarshrestha.loginregistration.service.UserService;
import np.com.oskarshrestha.loginregistration.util.ChangeUserPasswordStatus;
import np.com.oskarshrestha.loginregistration.util.EmailVerificationTokenStatus;
import np.com.oskarshrestha.loginregistration.util.ForgetPasswordEmailStatus;
import np.com.oskarshrestha.loginregistration.util.ResendVerifyEmailStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    AuthenticationController authenticationController;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
    }

    // tests for AuthenticationController().registerUser
    @Test
    public void whenNewUserRegisterRequest_returnRegistrationResponseSuccessTrue() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        UserRegisterRequest userRegisterRequest = Mockito.mock(UserRegisterRequest.class);

        // mock
        Mockito.when(userService.registerUser(userRegisterRequest, request)).thenReturn(
                RegistrationResponse.builder()
                        .existingUser(false)
                        .registrationSuccess(true)
                        .build()
        );

        // call method to test
        ResponseEntity<RegistrationResponse> response = authenticationController.registerUser(
                userRegisterRequest,
                request
        );

        // assertions
        Mockito.verify(userService).registerUser(userRegisterRequest, request);
        assertTrue(response.getBody().isRegistrationSuccess());
        assertFalse(response.getBody().isExistingUser());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void whenExistingUserRegisterRequest_returnRegistrationResponseSuccessFalse() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        UserRegisterRequest userRegisterRequest = Mockito.mock(UserRegisterRequest.class);

        // mock
        Mockito.when(userService.registerUser(userRegisterRequest, request)).thenReturn(
                RegistrationResponse.builder()
                        .existingUser(true)
                        .registrationSuccess(false)
                        .build()
        );

        // call method to test
        ResponseEntity<RegistrationResponse> response = authenticationController.registerUser(
                userRegisterRequest,
                request
        );

        // assertions
        Mockito.verify(userService).registerUser(userRegisterRequest, request);
        assertFalse(response.getBody().isRegistrationSuccess());
        assertTrue(response.getBody().isExistingUser());
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    // tests for AuthenticationController().verifyEmail
    @Test
    public void whenStatusIsValid_returnVerifyEmailResponseWithEmailVerificationTokenStatusValid() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String token = "123";

        // mock
        Mockito.when(userService.verifyEmailToken(token)).thenReturn(EmailVerificationTokenStatus.VALID);

        // call method to test
        ResponseEntity<VerifyEmailResponse> response = authenticationController.verifyEmail(token);

        // assertions
        Mockito.verify(userService).verifyEmailToken(token);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(EmailVerificationTokenStatus.VALID, Objects.requireNonNull(response.getBody()).getEmailVerificationTokenStatus());
    }

    @Test
    public void whenStatusIsInvalid_returnVerifyEmailResponseWithEmailVerificationTokenStatusInvalid() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String token = "123";

        // mock
        Mockito.when(userService.verifyEmailToken(token)).thenReturn(EmailVerificationTokenStatus.INVALID);

        // call method to test
        ResponseEntity<VerifyEmailResponse> response = authenticationController.verifyEmail(token);

        // assertions
        Mockito.verify(userService).verifyEmailToken(token);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(EmailVerificationTokenStatus.INVALID, Objects.requireNonNull(response.getBody()).getEmailVerificationTokenStatus());
    }

    @Test
    public void whenStatusIsExpired_returnVerifyEmailResponseWithEmailVerificationTokenStatusExpired() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String token = "123";

        // mock
        Mockito.when(userService.verifyEmailToken(token)).thenReturn(EmailVerificationTokenStatus.EXPIRED);

        // call method to test
        ResponseEntity<VerifyEmailResponse> response = authenticationController.verifyEmail(token);

        // assertions
        Mockito.verify(userService).verifyEmailToken(token);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(EmailVerificationTokenStatus.EXPIRED, Objects.requireNonNull(response.getBody()).getEmailVerificationTokenStatus());
    }

    @Test
    public void whenStatusIsDefault_returnVerifyEmailResponseWithEmailVerificationTokenStatusExpired() throws IllegalStateException {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String token = "123";

        // mock
        Mockito.when(userService.verifyEmailToken(token)).thenReturn(EmailVerificationTokenStatus.DEFAULT);

        // call method to test
        ResponseEntity<VerifyEmailResponse> response = authenticationController.verifyEmail(token);

        // assertions
        Mockito.verify(userService).verifyEmailToken(token);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    // tests for AuthenticationController().resendVerifyEmail

    @Test
    public void whenNewUser_returnResendVerifyEmailResponseWithResendVerifyEmailStatusSuccess() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String email = "hi@gmail.com";

        // mock
        Mockito.when(userService.resendVerificationEmail(email, request)).thenReturn(
                ResendVerifyEmailResponse
                        .builder()
                        .resendVerifyEmailStatus(ResendVerifyEmailStatus.SUCCESS)
                        .build()
        );

        // call method to test
        ResponseEntity<ResendVerifyEmailResponse> response = authenticationController.resendVerifyEmail(email, request);

        // assertions
        Mockito.verify(userService).resendVerificationEmail(email, request);
        assertEquals(ResendVerifyEmailStatus.SUCCESS, Objects.requireNonNull(response.getBody()).getResendVerifyEmailStatus());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void whenUserNotFound_returnResendVerifyEmailResponseWithResendVerifyEmailStatusEmailNotRegistered() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String email = "hi@gmail.com";

        // mock
        Mockito.when(userService.resendVerificationEmail(email, request)).thenReturn(
                ResendVerifyEmailResponse
                        .builder()
                        .resendVerifyEmailStatus(ResendVerifyEmailStatus.EMAIL_NOT_REGISTERED)
                        .build()
        );

        // call method to test
        ResponseEntity<ResendVerifyEmailResponse> response = authenticationController.resendVerifyEmail(email, request);

        // assertions
        Mockito.verify(userService).resendVerificationEmail(email, request);
        assertEquals(ResendVerifyEmailStatus.EMAIL_NOT_REGISTERED, Objects.requireNonNull(response.getBody()).getResendVerifyEmailStatus());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    // tests for AuthenticationController().changePassword

    @Test
    public void whenInvalidUser_returnChangePasswordResponseWithChangeUserPasswordStatusEmailNotFound() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String email = "hi@gmail.com";
        String oldPassword = "123";
        String newPassword = "456";
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest
                .builder()
                .email(email)
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();

        // mock
        Mockito.when(userService.changeUserPassword(
                        changePasswordRequest.getEmail(),
                        changePasswordRequest.getOldPassword(),
                        changePasswordRequest.getNewPassword()
                ))
                .thenReturn(ChangeUserPasswordStatus.EMAIL_NOT_FOUND);

        // call method to test
        ResponseEntity<ChangePasswordResponse> response = authenticationController.changePassword(changePasswordRequest);

        // assertions
        Mockito.verify(userService).changeUserPassword(
                changePasswordRequest.getEmail(),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
        assertEquals(ChangeUserPasswordStatus.EMAIL_NOT_FOUND, Objects.requireNonNull(response.getBody()).getChangeUserPasswordStatus());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void whenValidUserInvalidOldPassword_returnChangePasswordResponseWithChangeUserPasswordStatusPasswordMismatch() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String email = "hi@gmail.com";
        String oldPassword = "123";
        String newPassword = "456";
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest
                .builder()
                .email(email)
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();

        // mock
        Mockito.when(userService.changeUserPassword(
                        changePasswordRequest.getEmail(),
                        changePasswordRequest.getOldPassword(),
                        changePasswordRequest.getNewPassword()
                ))
                .thenReturn(ChangeUserPasswordStatus.PASSWORD_MISMATCH);

        // call method to test
        ResponseEntity<ChangePasswordResponse> response = authenticationController.changePassword(changePasswordRequest);

        // assertions
        Mockito.verify(userService).changeUserPassword(
                changePasswordRequest.getEmail(),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
        assertEquals(ChangeUserPasswordStatus.PASSWORD_MISMATCH, Objects.requireNonNull(response.getBody()).getChangeUserPasswordStatus());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void whenValidUserValidOldPassword_returnChangePasswordResponseWithChangeUserPasswordStatusSuccess() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String email = "hi@gmail.com";
        String oldPassword = "123";
        String newPassword = "456";
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest
                .builder()
                .email(email)
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();

        // mock
        Mockito.when(userService.changeUserPassword(
                        changePasswordRequest.getEmail(),
                        changePasswordRequest.getOldPassword(),
                        changePasswordRequest.getNewPassword()
                ))
                .thenReturn(ChangeUserPasswordStatus.SUCCESS);

        // call method to test
        ResponseEntity<ChangePasswordResponse> response = authenticationController.changePassword(changePasswordRequest);

        // assertions
        Mockito.verify(userService).changeUserPassword(
                changePasswordRequest.getEmail(),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
        assertEquals(ChangeUserPasswordStatus.SUCCESS, Objects.requireNonNull(response.getBody()).getChangeUserPasswordStatus());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    // tests for AuthenticationController().forgetPassword

    @Test
    public void whenUserNotFound_returnForgetPasswordResponseWithForgetPasswordEmailStatusEmailNotFound() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String email = "hi@gmail.com";

        // mock
        Mockito.when(userService.forgetPassword(email, request)).thenReturn(ForgetPasswordResponse
                .builder()
                .forgetPasswordEmailStatus(ForgetPasswordEmailStatus.EMAIL_NOT_FOUND)
                .build()
        );

        // call method to test
        ResponseEntity<ForgetPasswordResponse> response = authenticationController.forgetPassword(email, request);

        // assertions
        Mockito.verify(userService).forgetPassword(email, request);
        assertEquals(ForgetPasswordEmailStatus.EMAIL_NOT_FOUND, Objects.requireNonNull(response.getBody()).getForgetPasswordEmailStatus());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void whenValidUser_returnForgetPasswordResponseWithForgetPasswordEmailStatusSent() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        String email = "hi@gmail.com";

        // mock
        Mockito.when(userService.forgetPassword(email, request)).thenReturn(ForgetPasswordResponse
                .builder()
                .forgetPasswordEmailStatus(ForgetPasswordEmailStatus.SENT)
                .build()
        );

        // call method to test
        ResponseEntity<ForgetPasswordResponse> response = authenticationController.forgetPassword(email, request);

        // assertions
        Mockito.verify(userService).forgetPassword(email, request);
        assertEquals(ForgetPasswordEmailStatus.SENT, Objects.requireNonNull(response.getBody()).getForgetPasswordEmailStatus());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}