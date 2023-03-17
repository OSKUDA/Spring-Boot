package np.com.oskarshrestha.loginregistration.repository;

import np.com.oskarshrestha.loginregistration.entity.EmailVerificationToken;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmailVerificationTokenRepositoryTest {

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
       User user = User
                .builder()
                .email("hi@gmail.com")
                .enabled(true)
                .firstName("Hello")
                .lastName("Shrestha")
                .role(Role.USER)
                .password("$someEncodedLongString")
                .build();
        userRepository.save(user);
        EmailVerificationToken emailVerificationToken = EmailVerificationToken
                .builder()
                .token("123")
                .expirationTime(calculateExpirationTime(10))
                .user(user)
                .build();

        emailVerificationTokenRepository.save(emailVerificationToken);
    }

    @Test
    public void whenValidToken_returnValidEmailVerificationToken(){
        String tokenToTest = "123";
        EmailVerificationToken fetchData = emailVerificationTokenRepository.findByToken(tokenToTest);
        assertEquals(tokenToTest, fetchData.getToken());
    }

    @Test
    public void whenInvalidToken_returnNull(){
        String tokenToTest ="456";
        EmailVerificationToken fetchData = emailVerificationTokenRepository.findByToken(tokenToTest);
        assertNull(fetchData);
    }

    private Date calculateExpirationTime(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}