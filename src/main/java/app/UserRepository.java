package app;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u INNER JOIN u.department d WHERE d.name=:department")
    public List<User> findUsersFromDepartment(@Param("department") String department);

    @Query("SELECT e FROM User e WHERE e.pay > :payment")
    public List<User> findUsersWithPaymentIsHigher(@Param("payment") Double payment);
}
