import model.Department;
import model.User;

import lombok.extern.java.Log;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@Log
public class AppTest {

    private static Service service;

    public static void main(String[] args) {

        AppTest.startH2();

        service = new Service();

        User manA = service.createManager("Szymon", "Nowak");
        User manB = service.createManager("Kamil", "Owocny");
        User manC = service.createManager("Mateusz", "Wielki");

        Department departmentA = service.createDepartment("DepartmentA", manA);
        Department departmentB = service.createDepartment("DepartmentB", manB);
        Department departmentC = service.createDepartment("DepartmentC", manC);

        service.createUser("Karolina", "Bartnicka", departmentB);
        service.createUser("Piotr", "Borys", departmentB);
        service.createUser("Natalia", "Jagoda", departmentC);

        manA.setPay(15000.0);
        service.commit(manA);

        AppTest.checkQueries();
    }

    private static void startH2() {
        try {
            Server server = Server.createTcpServer("-tcpAllowOthers").start();
            System.out.println("H2 started on URL H2 = " + server.getURL());
            Class.forName("org.h2.Driver");

            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/rsuc", "rsuc", "rsuc");
            System.out.println("Connection Established: " + conn.getMetaData().getDatabaseProductName() + "/" + conn.getCatalog());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(9);
        }
    }

    private static void checkQueries() {
        System.out.println("Departamenty");
        List<Department> departments = service.findAllDepartments();
        departments.forEach(System.out::println);

        System.out.println("Userzy");
        List<User> users = service.findAllUsers();
        users.forEach(System.out::println);

        System.out.println("Department Kamila");
        Department departmentByUser = service.findDepartmentManagedByUser("Kamil", "Owocny");
        System.out.println(departmentByUser);

        System.out.println("Max userow");
        Department departmentByUsers = service.findDepartmentWithMaxNumberOfEmployes();
        System.out.println(departmentByUsers);

        System.out.println("Max salary");
        Department departmentBySalary = service.findDepartmentWithMaxUsersSalary();
        System.out.println(departmentBySalary);

        System.out.println("User Mateusz");
        User user = service.findUser("Mateusz", "Wielki", "DepartmentC");
        System.out.println(user);

        System.out.println("Users z DepartmentB");
        List<User> usersFromDepartment = service.findUsersFromDepartment("DepartmentB");
        usersFromDepartment.forEach(System.out::println);

        System.out.println("Users z pay > 2000");
        List<User> usersByPayment = service.findUsersWithPaymentIsHigher(2000.0);
        usersByPayment.forEach(System.out::println);
    }
}