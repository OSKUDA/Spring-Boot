package np.com.oskarshrestha.loginregistration.service;

import np.com.oskarshrestha.loginregistration.entity.EmailVerificationToken;
import np.com.oskarshrestha.loginregistration.util.EmailVerificationTokenStatus;
import np.com.oskarshrestha.loginregistration.util.Role;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.model.AuthenticationResponse;
import np.com.oskarshrestha.loginregistration.model.RegistrationResponse;
import np.com.oskarshrestha.loginregistration.model.UserAuthenticationRequest;
import np.com.oskarshrestha.loginregistration.model.UserRegisterRequest;
import np.com.oskarshrestha.loginregistration.repository.EmailVerificationTokenRepository;
import np.com.oskarshrestha.loginregistration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

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

        String jwtToken = jwtService.generateToken(user);

        return RegistrationResponse
                .builder()
                .token(jwtToken)
                .user(user)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(UserAuthenticationRequest userAuthenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userAuthenticationRequest.getEmail(),
                        userAuthenticationRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(userAuthenticationRequest.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken(user, token);
        emailVerificationTokenRepository.save(emailVerificationToken);
    }

    @Override
    public EmailVerificationTokenStatus verifyEmailToken(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token);

        if(verificationToken == null){
            return EmailVerificationTokenStatus.INVALID;
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            emailVerificationTokenRepository.delete(verificationToken);
            return EmailVerificationTokenStatus.EXPIRED;
        }

        user.setEnabled(true);
        userRepository.save(user);

        return EmailVerificationTokenStatus.VALID;
    }
}
