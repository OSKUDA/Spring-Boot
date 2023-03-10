package np.com.oskarshrestha.loginregistration.service;

import np.com.oskarshrestha.loginregistration.entity.Role;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.model.RegistrationResponse;
import np.com.oskarshrestha.loginregistration.model.UserRegisterRequest;
import np.com.oskarshrestha.loginregistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RegistrationResponse registerUser(UserRegisterRequest userRegisterRequest) {
        User user = new User();
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setLastName(userRegisterRequest.getLastName());
        user.setEmail(userRegisterRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        user.setEnabled(false);

        userRepository.save(user);
        return RegistrationResponse.builder().token("SUCCESS").user(user).build();
    }
}
