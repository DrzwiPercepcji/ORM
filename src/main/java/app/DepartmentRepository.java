package app;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import org.springframework.data.domain.Pageable;

@RepositoryRestResource(collectionResourceRel = "department", path = "department")
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d INNER JOIN d.manager u WHERE u.firstname=:firstname AND u.lastname=:lastname")
    public List<Department> findDepartmentManagedByUser(@Param("firstname") String firstname, @Param("lastname") String lastname);

    @Query("SELECT u.department FROM User u INNER JOIN u.department d GROUP BY u.department ORDER BY COUNT(u.id) DESC")
    public List<Department> findDepartmentWithMaxNumberOfEmployes(Pageable pageable);

    @Query("SELECT u.department FROM User u INNER JOIN u.department d GROUP BY u.department ORDER BY SUM(u.pay) DESC")
    public List<Department> findDepartmentWithMaxUsersSalary(Pageable pageable);
}
