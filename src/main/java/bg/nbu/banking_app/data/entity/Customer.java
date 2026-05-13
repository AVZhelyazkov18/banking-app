package bg.nbu.banking_app.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "customer")
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer extends BaseEntity {
    @Column(name = "client_number", unique = true)
    private String clientNumber;

    @OneToMany(mappedBy = "customer")
    private Set<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "customer")
    private Set<Loan> loans;
}
