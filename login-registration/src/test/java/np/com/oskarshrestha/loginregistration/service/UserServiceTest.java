package np.com.oskarshrestha.loginregistration.service;

import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.repository.UserRepository;
import np.com.oskarshrestha.loginregistration.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @BeforeEach
    void setUp() {
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
    }

    @Test
    @DisplayName("Get user data based on valid email")
    public void whenValidEmail_thenUserShouldBeFound(){
        String email = "hi@gmail.com";
        Optional<User> user = userService.getUserByEmail(email);
        assertEquals(email, user.get().getEmail());
    }

}