package np.com.oskarshrestha.oauthserver.repository;

import np.com.oskarshrestha.oauthserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public User findByEmail(String email);
}
