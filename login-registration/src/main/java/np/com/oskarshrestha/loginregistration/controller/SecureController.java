package np.com.oskarshrestha.loginregistration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

    @GetMapping("/secure/hello")
    public String hello(){
        return "Hello";
    }
}
