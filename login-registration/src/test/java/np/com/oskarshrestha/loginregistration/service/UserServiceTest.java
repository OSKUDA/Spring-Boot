package np.com.oskarshrestha.loginregistration.service;

import np.com.oskarshrestha.loginregistration.entity.EmailVerificationToken;
import np.com.oskarshrestha.loginregistration.entity.ForgetPasswordToken;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.repository.EmailVerificationTokenRepository;
import np.com.oskarshrestha.loginregistration.repository.ForgetPasswordTokenRepository;
import np.com.oskarshrestha.loginregistration.repository.UserRepository;
import np.com.oskarshrestha.loginregistration.util.ChangeUserPasswordStatus;
import np.com.oskarshrestha.loginregistration.util.EmailVerificationTokenStatus;
import np.com.oskarshrestha.loginregistration.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private ForgetPasswordTokenRepository forgetPasswordTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        // mock user as valid user
        User user = User
                .builder()
                .email("hi@gmail.com")
                .id(1L)
                .enabled(true)
                .firstName("Hello")
                .lastName("Shrestha")
                .role(Role.USER)
                .password("$someEncodedLongString")
                .build();
        Mockito.when(userRepository.findByEmail("hi@gmail.com")).thenReturn(Optional.ofNullable(user));

        String emailToTest = "hi@gmail.com";
        Optional<User> fetchData = userService.getUserByEmail(emailToTest);
        assertEquals(emailToTest, fetchData.get().getEmail());
    }

    @Test
    public void whenInvalidEmail_returnNull() {
        // mock user as invalid user
        Mockito.when(userRepository.findByEmail("oskar@gmail.com")).thenReturn(Optional.empty());

        String emailToTest = "oskar@gmail.com";
        Optional<User> fetchData = userService.getUserByEmail(emailToTest);
        assertTrue(fetchData.isEmpty());
    }

    @Test
    public void whenValidVerifyEmailToken_returnEmailVerificationTokenStatusValid() {
        // mock
        User user = User
                .builder()
                .id(1L)
                .firstName("Oskar")
                .lastName("Shrestha")
                .email("oskar@gmail.com")
                .password("{none}somePassword")
                .role(Role.USER)
                .enabled(false)
                .build();
        EmailVerificationToken emailVerificationToken =
                EmailVerificationToken
                        .builder()
                        .id(1L)
                        .token("123")
                        .expirationTime(new Date(System.currentTimeMillis() + (24 * 60 * 60)))
                        .user(user)
                        .build();
        Mockito.when(emailVerificationTokenRepository.findByToken("123"))
                .thenReturn(emailVerificationToken);
        user.setEnabled(true);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        String tokenToTest = "123";
        EmailVerificationTokenStatus returnedEmailVerificationTokenStatus = userService.verifyEmailToken(tokenToTest);

        assertEquals(EmailVerificationTokenStatus.VALID, returnedEmailVerificationTokenStatus);
    }

    @Test
    public void whenExpiredVerifyEmailToken_returnEmailVerificationTokenStatusExpired() {
        // mock
        EmailVerificationToken emailVerificationToken =
                EmailVerificationToken
                        .builder()
                        .id(1L)
                        .token("123")
                        .expirationTime(new Date())
                        .user(
                                User
                                        .builder()
                                        .id(1L)
                                        .firstName("Oskar")
                                        .lastName("Shrestha")
                                        .email("oskar@gmail.com")
                                        .password("{none}somePassword")
                                        .role(Role.USER)
                                        .enabled(false)
                                        .build()
                        )
                        .build();
        Mockito.when(emailVerificationTokenRepository.findByToken("123"))
                .thenReturn(emailVerificationToken);

        String tokenToTest = "123";
        EmailVerificationTokenStatus fetchData = userService.verifyEmailToken(tokenToTest);
        assertEquals(EmailVerificationTokenStatus.EXPIRED, fetchData);
    }

    @Test
    public void whenInvalidVerifyEmailToken_returnEmailVerificationTokenStatusInvalid() {
        // mock
        Mockito.when(emailVerificationTokenRepository.findByToken("123"))
                .thenReturn(null);

        String tokenToTest = "123";
        EmailVerificationTokenStatus returnedEmailVerificationTokenStatus = userService.verifyEmailToken(tokenToTest);
        assertEquals(EmailVerificationTokenStatus.INVALID, returnedEmailVerificationTokenStatus);
    }

    @Test
    public void whenInvalidEmail_returnChangeUserPasswordStatusEmailNotFound() {
        // mock
        Mockito.when(userRepository.findByEmail("hi@gmail.com")).thenReturn(Optional.empty());

        ChangeUserPasswordStatus fetchData = userService.changeUserPassword(
                "hi@gmail.com",
                "123",
                "456"
        );
        assertEquals(ChangeUserPasswordStatus.EMAIL_NOT_FOUND, fetchData);
    }

    @Test
    public void whenValidEmailInvalidOldPassword_returnChangeUserPasswordStatusPasswordMismatch() {
        // mock
        User user = User
                .builder()
                .id(1L)
                .firstName("Oskar")
                .lastName("Shrestha")
                .email("hi@gmail.com")
                .password("123")
                .role(Role.USER)
                .enabled(true)
                .build();
        Mockito.when(userRepository.findByEmail("hi@gmail.com")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("456", "123")).thenReturn(false);

        ChangeUserPasswordStatus fetchData = userService.changeUserPassword(
                "hi@gmail.com",
                "456",
                "123"
        );
        assertEquals(ChangeUserPasswordStatus.PASSWORD_MISMATCH, fetchData);
    }

    @Test
    public void whenValidEmailValidOldPassword_returnChangeUserPasswordStatusSuccess() {
        // mock
        User user = User
                .builder()
                .id(1L)
                .firstName("Oskar")
                .lastName("Shrestha")
                .email("hi@gmail.com")
                .password("123")
                .role(Role.USER)
                .enabled(true)
                .build();
        Mockito.when(userRepository.findByEmail("hi@gmail.com")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("123", "123")).thenReturn(true);
        Mockito.when(passwordEncoder.encode("456")).thenReturn("456");

        ChangeUserPasswordStatus fetchData = userService.changeUserPassword(
                "hi@gmail.com",
                "123",
                "456"
        );

        assertEquals(ChangeUserPasswordStatus.SUCCESS, fetchData);
    }

    @Test
    public void whenUserNull_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            userService.saveForgetPasswordToken("123", null);
        });
    }

    @Test
    public void whenTokenNull_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            userService.saveForgetPasswordToken(null, mock(User.class)
            );
        });
    }

    @Test
    public void whenForgetPasswordTokenNotFound_createNewForgetPasswordToken() {
        // arrange
        User userToTest = mock(User.class);
        String token = "123";

        // mock repository behaviour
        Mockito.when(forgetPasswordTokenRepository.findByUser(userToTest)).thenReturn(null);

        // call method to test
        userService.saveForgetPasswordToken(token,userToTest);

        // assertions
        ArgumentCaptor<ForgetPasswordToken> captor = ArgumentCaptor.forClass(ForgetPasswordToken.class);
        verify(forgetPasswordTokenRepository).findByUser(userToTest);
        verify(forgetPasswordTokenRepository).save(captor.capture());

        ForgetPasswordToken savedToken = captor.getValue();
        assertEquals(userToTest, savedToken.getUser());
        assertEquals(token, savedToken.getToken());
    }

    @Test
    public void whenForgetPasswordTokenFound_updateExistingForgetPasswordToken(){
        // arrange
        User userToTest = User
                .builder()
                .id(1L)
                .firstName("Oskar")
                .lastName("Shrestha")
                .email("hi@gmail.com")
                .password("123")
                .role(Role.USER)
                .enabled(true)
                .build();
        String tokenToTest = "456";
        ForgetPasswordToken forgetPasswordToken = new ForgetPasswordToken(userToTest, "123");

        // mock repository behaviour
        Mockito.when(forgetPasswordTokenRepository.findByUser(userToTest)).thenReturn(forgetPasswordToken);

        // call method to test
        userService.saveForgetPasswordToken(tokenToTest,userToTest);

        ArgumentCaptor<ForgetPasswordToken> captor = ArgumentCaptor.forClass(ForgetPasswordToken.class);
        verify(forgetPasswordTokenRepository).findByUser(userToTest);
        verify(forgetPasswordTokenRepository).save(captor.capture());

        ForgetPasswordToken savedToken = captor.getValue();
        assertEquals(tokenToTest, savedToken.getToken());
        assertEquals(userToTest, savedToken.getUser());
    }



}