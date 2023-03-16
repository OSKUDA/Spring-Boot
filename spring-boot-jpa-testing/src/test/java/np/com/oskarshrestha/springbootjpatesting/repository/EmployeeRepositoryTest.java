package np.com.oskarshrestha.springbootjpatesting.repository;

import np.com.oskarshrestha.springbootjpatesting.entity.Employee;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.assertj.core.api.Assertions;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("Oskar")
                .lastName("Shrestha")
                .email("oskar@gmail.com")
                .build();
    }

    // JUnit test for saveEmployee
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveEmployeeTest(){
        employeeRepository.save(employee);
        Assertions.assertThat(employee.getId()).isGreaterThan(0);
    }

    // JUnit test for getEmployee
    @Test
    @Order(2)
    public void getEmployeeTest(){
        Employee employee = employeeRepository.findById(1L).get();

        Assertions.assertThat(employee.getId()).isEqualTo(1L);
    }

    // JUnit test for getAllEmployees
    @Test
    @Order(3)
    public void getListOfEmployeesTest(){
        List<Employee> employees = employeeRepository.findAll();

        Assertions.assertThat(employees.size()).isGreaterThan(0);
    }

    // JUnit test for updateEmployee
    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateEmployeeTest(){
        Employee fetchedEmployee = employeeRepository.findById(1L).get();
        fetchedEmployee.setEmail("ram@gmail.com");

        Employee updatedEmployee = employeeRepository.save(fetchedEmployee);
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("ram@gmail.com");
    }

    // JUnit test for deleteEmployee
    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteEmployeeTest(){
        Employee fetchedEmployee = employeeRepository.findById(1L).get();
        employeeRepository.delete(fetchedEmployee);

        Employee employee1 = null;
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail("ram@gmail.com");

        if(optionalEmployee.isPresent()){
            employee1 = optionalEmployee.get();
        }
        Assertions.assertThat(employee1).isNull();
    }

}