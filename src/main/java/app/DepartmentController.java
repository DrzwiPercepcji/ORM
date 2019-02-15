package app;

import java.util.List;
import javax.inject.Inject;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.PageRequest;

@RestController
public class DepartmentController {
    private final DepartmentRepository repository;

    @Inject
    UserRepository userRepository;

    DepartmentController(DepartmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/departments")
    List<Department> all() {
        return repository.findAll();
    }

    @GetMapping("/departments/{id}")
    Department get(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @GetMapping("/departments/employes")
    List<Department> employes() {

        return repository.findDepartmentWithMaxNumberOfEmployes(new PageRequest(0, 1));
    }

    @GetMapping("/departments/salary")
    List<Department> salary() {

        return repository.findDepartmentWithMaxUsersSalary(new PageRequest(0, 1));
    }

    @GetMapping("/departments/user/{first}-{last}")
    List<Department> user(@PathVariable String first, @PathVariable String last) {

        return repository.findDepartmentManagedByUser(first, last);
    }

    @PostMapping("/departments")
    Department add(@RequestBody Department department) {
        Long userId = department.getManager().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        department.setManager(user);
        return repository.save(department);
    }

    @DeleteMapping("/departments/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("/departments/{id}")
    Department replace(@RequestBody Department modified, @PathVariable Long id) {

        return repository.findById(id)
                .map(department -> {
                    department.setName(modified.getName());
                    department.setDescription(modified.getDescription());
                    return repository.save(department);
                })
                .orElseGet(() -> {
                    modified.setId(id);
                    return repository.save(modified);
                });
    }
}