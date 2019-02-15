package app;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "departments")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Department {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.PERSIST, optional = false)
    private User manager;

    //private Integer managerId;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "phone", length = 100)
    private String phone;

    @Column(name = "mail", length = 100)
    private String mail;

    @Column(name = "www", length = 100)
    private String www;

    @Column(name = "description")
    private String description;

    public void setManager(User user) {
        manager = user;
        //managerId = user.getId();
    }
}