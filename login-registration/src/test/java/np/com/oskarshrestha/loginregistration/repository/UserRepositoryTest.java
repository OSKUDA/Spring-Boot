package np.com.oskarshrestha.loginregistration.repository;

import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
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
    }

    @Test
    public void whenValidEmail_returnValidUser(){
        String emailToTest = "hi@gmail.com";
        Optional<User> user = userRepository.findByEmail(emailToTest);
        user.ifPresent(userData -> assertEquals("hi@gmail.com", userData.getEmail()));
    }

    @Test
    public void whenInvalidEmail_returnNull(){
        String emailToTest = "oskar@gmail.com";
        Optional<User> user = userRepository.findByEmail(emailToTest);
        assertTrue(user.isEmpty());
    }

    @Test
    public void whenValidEmail_returnExistsTrue(){
        String emailToTest = "hi@gmail.com";
        assertTrue(userRepository.existsByEmail(emailToTest));
    }

    @Test
    public void whenInValidEmail_returnExistsFalse(){
        String emailToTest = "oskar@gmail.com";
        assertFalse(userRepository.existsByEmail(emailToTest));
    }


}