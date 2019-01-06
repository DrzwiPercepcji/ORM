import model.Department;
import model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

class Service {
    private EntityManager em;

    Service() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DepartmentService");

        em = emf.createEntityManager();
    }

    boolean commit(Object object) {
        try {
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    Department createDepartment(String name, User user) {
        Department department = new Department();

        department.setName(name);
        department.setManager(user);

        if (commit(department)) {
            user.setDepartment(department);
            commit(user);

            return department;
        }

        return null;
    }

    User createManager(String firstname, String lastname) {
        User user = new User();

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPay(2500.0);

        return commit(user) ? user : null;
    }

    User createUser(String firstname, String lastname, Department department) {
        User user = new User();

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setDepartment(department);
        user.setPay(1900.0);

        return commit(user) ? user : null;
    }

    List<Department> findAllDepartments() {
        return em.createQuery("SELECT d FROM Department d", Department.class).getResultList();
    }

    List<User> findAllUsers() {
        return em.createQuery("SELECT e FROM User e", User.class).getResultList();
    }

    Department findDepartmentManagedByUser(String firstname, String lastname) {
        Department department = em.createQuery("SELECT d FROM Department d INNER JOIN d.manager u WHERE u.firstname=:firstname AND u.lastname=:lastname", Department.class)
                .setParameter("firstname", firstname)
                .setParameter("lastname", lastname)
                .getSingleResult();

        return department;
    }

    Department findDepartmentWithMaxNumberOfEmployes() {
        Department department = em.createQuery("SELECT u.department FROM User u INNER JOIN u.department d GROUP BY u.department ORDER BY COUNT(*) DESC", Department.class)
                .setMaxResults(1)
                .getSingleResult();

        return department;
    }

    Department findDepartmentWithMaxUsersSalary() {
        Department department = em.createQuery("SELECT u.department FROM User u INNER JOIN u.department d GROUP BY u.department ORDER BY SUM(u.pay) DESC", Department.class)
                .setMaxResults(1)
                .getSingleResult();

        return department;
    }

    User findUser(String firstname, String lastname, String department) {
        User user = em.createQuery("SELECT u FROM User u INNER JOIN u.department d WHERE u.firstname=:firstname AND u.lastname=:lastname AND d.name=:department", User.class)
                .setParameter("firstname", firstname)
                .setParameter("lastname", lastname)
                .setParameter("department", department)
                .getSingleResult();

        return user;
    }

    List<User> findUsersFromDepartment(String department) {
        return em.createQuery("SELECT u FROM User u INNER JOIN u.department d WHERE d.name=:department", User.class)
                .setParameter("department", department)
                .getResultList();
    }

    List<User> findUsersWithPaymentIsHigher(Double payment) {
        return em.createQuery("SELECT e FROM User e WHERE e.pay > " + payment.toString(), User.class).getResultList();
    }
}
