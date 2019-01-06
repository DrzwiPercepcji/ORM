package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Department department;

    private Integer departmentId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "firstname", length = 100)
    private String firstname;

    @Column(name = "lastname", length = 100)
    private String lastname;

    @Column(name = "description")
    private String description;

    @Column(name = "pay")
    private Double pay;

    @Column(name = "bonus")
    private String bonus;

    @Column(name = "date")
    private Date date;

    public void setDepartment(Department department) {
        this.department = department;
        departmentId = department.getId();
    }
}