package np.com.oskarshrestha.loginregistration.repository;

import np.com.oskarshrestha.loginregistration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
