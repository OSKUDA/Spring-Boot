package np.com.oskarshrestha.loginregistration.service;

import np.com.oskarshrestha.loginregistration.entity.EmailVerificationToken;
import np.com.oskarshrestha.loginregistration.entity.ForgetPasswordToken;
import np.com.oskarshrestha.loginregistration.entity.User;
import np.com.oskarshrestha.loginregistration.model.AuthenticationResponse;
import np.com.oskarshrestha.loginregistration.model.RegisterUserResponse;
import np.com.oskarshrestha.loginregistration.model.UserAuthenticationRequest;
import np.com.oskarshrestha.loginregistration.model.UserRegisterRequest;
import np.com.oskarshrestha.loginregistration.repository.EmailVerificationTokenRepository;
import np.com.oskarshrestha.loginregistration.repository.ForgetPasswordTokenRepository;
import np.com.oskarshrestha.loginregistration.repository.UserRepository;
import np.com.oskarshrestha.loginregistration.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private ForgetPasswordTokenRepository forgetPasswordTokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public RegisterUserResponse registerUser(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            return RegisterUserResponse
                    .builder()
                    .existingUser(true)
                    .build();
        }

        User user = new User();
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setLastName(userRegisterRequest.getLastName());
        user.setEmail(userRegisterRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return RegisterUserResponse
                .builder()
                .token(jwtToken)
                .user(user)
                .existingUser(false)
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
        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByUser(user);

        // create new entry
        if (emailVerificationToken == null) {
            emailVerificationToken = new EmailVerificationToken(user, token);
            emailVerificationTokenRepository.save(emailVerificationToken);
            return;
        }

        // update existing entry
        emailVerificationToken.setToken(token);
        emailVerificationToken.setExpirationTime();
        emailVerificationTokenRepository.save(emailVerificationToken);
    }

    @Override
    public EmailVerificationTokenStatus verifyEmailToken(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return EmailVerificationTokenStatus.INVALID;
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            emailVerificationTokenRepository.delete(verificationToken);
            return EmailVerificationTokenStatus.EXPIRED;
        }

        user.setEnabled(true);
        userRepository.save(user);

        return EmailVerificationTokenStatus.VALID;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public ChangeUserPasswordStatus changeUserPassword(String email, String oldPassword, String newPassword) {
        // check if old password matches the email
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ChangeUserPasswordStatus.EMAIL_NOT_FOUND;
        }

        if (!passwordEncoder.matches(oldPassword, user.get().getPassword())) {
            return ChangeUserPasswordStatus.PASSWORD_MISMATCH;
        }

        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());
        return ChangeUserPasswordStatus.SUCCESS;
    }

    @Override
    public void saveForgetPasswordToken(String token, User user) {
        ForgetPasswordToken forgetPasswordToken = forgetPasswordTokenRepository.findByUser(user);

        // create new entry
        if(forgetPasswordToken == null){
            forgetPasswordToken = new ForgetPasswordToken(user, token);
            forgetPasswordTokenRepository.save(forgetPasswordToken);
            return;
        }
            // update existing entry
            forgetPasswordToken.setToken(token);
            forgetPasswordToken.setExpirationTime();
            forgetPasswordTokenRepository.save(forgetPasswordToken);
    }


    @Override
    public ResetPasswordResponseStatus resetUserPassword(String token,String password) {

        ForgetPasswordToken forgetPasswordToken = forgetPasswordTokenRepository.findByToken(token);


        if(forgetPasswordToken == null){
            return ResetPasswordResponseStatus.TOKEN_NOT_FOUND;
        }

        if(!forgetPasswordToken.getToken().equals(token)){
            return ResetPasswordResponseStatus.INVALID;
        }

        Calendar calendar = Calendar.getInstance();
        if((forgetPasswordToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <=0){
            forgetPasswordTokenRepository.delete(forgetPasswordToken);
            return ResetPasswordResponseStatus.EXPIRED;
        }

        User user = forgetPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return ResetPasswordResponseStatus.SUCCESS;
    }

}
