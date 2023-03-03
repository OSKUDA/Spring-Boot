package np.com.oskarshrestha.springsecurityclient.service;

import np.com.oskarshrestha.springsecurityclient.entity.User;
import np.com.oskarshrestha.springsecurityclient.entity.VerificationToken;
import np.com.oskarshrestha.springsecurityclient.model.UserModel;

import java.util.Optional;

public interface UserService {
    public User registerUser(UserModel userModel);

    public void saveVerificationTokenForUser(String token, User user);

    public String validateVerificationToken(String token);

    public VerificationToken generateNewVerificationToken(String oldToken);

    public User findUserByEmail(String email);

    public void createPasswordResetTokenForUser(User user, String token);

    public String validatePasswordResetToken(String token);

    public Optional<User> getUserByPasswordResetToken(String token);

    public void changePassword(User user, String newPassword);

    public boolean checkIfValidOldPassword(User user, String oldPassword);
}
