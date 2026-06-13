package bg.nbu.banking_app.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Person extends Customer {
    @Setter
    @Column(name = "first_name")
    private String firstName;

    @Setter
    @Column(name = "last_name")
    private String lastName;

    @Setter
    @Column(name = "pin")
    private String pin;

    @Setter
    @Column(name = "phone")
    private String phone;

    @Setter
    @Column(name = "address")
    private String address;
}
