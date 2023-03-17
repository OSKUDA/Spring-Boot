package np.com.oskarshrestha.loginregistration.repository;

import np.com.oskarshrestha.loginregistration.entity.ForgetPasswordToken;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ForgetPasswordTokenRepositoryTest {

    @Autowired
    private ForgetPasswordTokenRepository forgetPasswordTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private ForgetPasswordToken forgetPasswordToken;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                User
                        .builder()
                        .email("hi@gmail.com")
                        .enabled(true)
                        .firstName("Hello")
                        .lastName("Shrestha")
                        .role(Role.USER)
                        .password("$someEncodedLongString")
                        .build()
        );
        userRepository.save(user);
        forgetPasswordToken = forgetPasswordTokenRepository.save(
                ForgetPasswordToken
                        .builder()
                        .token("123")
                        .expirationTime(calculateExpirationTime(10))
                        .user(user)
                        .build()
        );
        forgetPasswordTokenRepository.save(forgetPasswordToken);
    }

    @Test
    public void whenValidToken_returnValidForgetPasswordToken() {
        String tokenToTest = "123";
        ForgetPasswordToken fetchData = forgetPasswordTokenRepository.findByToken(tokenToTest);
        assertEquals(tokenToTest, fetchData.getToken());
    }

    @Test
    public void whenInvalidToken_returnNull(){
        String tokenToTest = "456";
        ForgetPasswordToken fetchData = forgetPasswordTokenRepository.findByToken(tokenToTest);
        assertNull(fetchData);
    }

    @Test
    public void whenValidUser_returnValidForgetPasswordToken(){
        User userToTest = user;
        ForgetPasswordToken fetchData = forgetPasswordTokenRepository.findByUser(userToTest);
        assertEquals(userToTest, fetchData.getUser());
    }

    @Test
    public void whenInvalidUser_returnNull(){
        User userToTest = User
                .builder()
                .firstName("Oskar")
                .lastName("Shrestha")
                .email("oskar@gmail.com")
                .role(Role.USER)
                .password("{noon}password")
                .enabled(true)
                .build();
        userRepository.save(userToTest);
        ForgetPasswordToken fetchData = forgetPasswordTokenRepository.findByUser(userToTest);
        assertNull(fetchData);
    }

    private Date calculateExpirationTime(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}