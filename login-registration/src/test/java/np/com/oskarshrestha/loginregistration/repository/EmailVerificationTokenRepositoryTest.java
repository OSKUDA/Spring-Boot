package np.com.oskarshrestha.loginregistration.repository;

import np.com.oskarshrestha.loginregistration.entity.EmailVerificationToken;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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

    private User user;
    private EmailVerificationToken emailVerificationToken;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User
                .builder()
                .email("hi@gmail.com")
                .enabled(true)
                .firstName("Hello")
                .lastName("Shrestha")
                .role(Role.USER)
                .password("$someEncodedLongString")
                .build()
        );
        emailVerificationToken = emailVerificationTokenRepository.save(EmailVerificationToken
                .builder()
                .token("123")
                .expirationTime(calculateExpirationTime(10))
                .user(user)
                .build());
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

    @Test
    public void whenValidUser_returnValidEmailVerificationToken(){
        User userToTest = user;
        EmailVerificationToken fetchData = emailVerificationTokenRepository.findByUser(userToTest);
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
        EmailVerificationToken fetchData = emailVerificationTokenRepository.findByUser(userToTest);
        assertNull(fetchData);
    }

    private Date calculateExpirationTime(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
};