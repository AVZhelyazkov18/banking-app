package bg.nbu.banking_app.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "customer")
public class Customer extends BaseEntity {
    @OneToMany(mappedBy = "customer")
    private Set<BankAccount> bankAccounts;
}
