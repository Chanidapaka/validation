package sit.int204.sampleexception.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @Column(name = "employeeNumber", nullable = false)
    private Integer employeeNumber;

    @Size(max = 50)
    @NotNull
    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Size(max = 50)
    @NotNull
    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Size(max = 10)
    @NotNull
    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportsTo")
    private Employee reportsTo;

    @Size(max = 50)
    @NotNull
    @Column(name = "jobTitle", nullable = false, length = 50)
    private String jobTitle;

}