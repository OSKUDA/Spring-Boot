package np.com.oskarshrestha.springbootjpatesting.repository;

import np.com.oskarshrestha.springbootjpatesting.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Optional<Employee> findByEmail(String email);
}
