package bg.nbu.banking_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Person {
    @Id
    @PrimaryKeyJoinColumn(name = "id")
    @OneToOne
    private Customer customer;
    
    @Setter
    @Column(name = "first_name")
    private String firstName;

    @Setter
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "pin")
    private String pin;
}
