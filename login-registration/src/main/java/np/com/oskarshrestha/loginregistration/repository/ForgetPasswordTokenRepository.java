package np.com.oskarshrestha.loginregistration.repository;

import np.com.oskarshrestha.loginregistration.entity.ForgetPasswordToken;
import np.com.oskarshrestha.loginregistration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgetPasswordTokenRepository extends JpaRepository<ForgetPasswordToken, Long> {
    public ForgetPasswordToken findByToken(String token);

    public ForgetPasswordToken findByUser(User user);
}
