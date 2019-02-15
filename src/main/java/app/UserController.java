package app;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class UserController {
    private final UserRepository repository;

    @Inject
    DepartmentRepository departmentRepository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }

    @GetMapping("/users/from/{department}")
    List<User> from(@PathVariable String department) {
        return repository.findUsersFromDepartment(department);
    }

    @GetMapping("/users/payment/{payment}")
    List<User> payment(@PathVariable Double payment) {
        return repository.findUsersWithPaymentIsHigher(payment);
    }

    @GetMapping("/users/{id}")
    User get(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("/users")
    User add(@RequestBody User user) {

        try {
            Long departmentId = user.getDepartment().getId();
            Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new UserNotFoundException(departmentId));
            user.setDepartment(department);
        } catch (NullPointerException e) {
        }

        return repository.save(user);
    }

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("/users/{id}")
    User replace(@RequestBody User modified, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    user.setFirstname(modified.getFirstname());
                    user.setLastname(modified.getLastname());
                    user.setPay(modified.getPay());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    modified.setId(id);
                    return repository.save(modified);
                });
    }
}