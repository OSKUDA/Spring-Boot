package np.com.oskarshrestha.loginregistration.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SecureControllerTest {

    @InjectMocks
    SecureController secureController;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void sayHello() {
        // arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // call method to test
        String response = secureController.hello();
        System.out.println(response);

        // assertions
        assertEquals("Hello",response);
    }


}