package np.com.oskarshrestha.springsecurityclient.repository;

import np.com.oskarshrestha.springsecurityclient.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    public PasswordResetToken findByToken(String token);
}
