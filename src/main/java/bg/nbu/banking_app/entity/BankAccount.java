package bg.nbu.banking_app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
public class BankAccount extends BaseEntity {
    @Column(name = "iban")
    private String iban;

    @Setter
    @Column(name = "balance")
    private BigDecimal balance;
    
    @Column(name = "status")
    private boolean status;
    
    @ManyToOne
    private Customer customer;
}
