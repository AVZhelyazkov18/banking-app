package bg.nbu.banking_app.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
public class Loan extends BaseEntity {
    @Column(name = "amount_disburesd")
    private BigDecimal amountDisbursed;

    @Column(name = "payment_term")
    private int paymentTerm;

    @ManyToOne
    private LoanType loanType;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "loan", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<PaymentPlan> paymentPlans;
}
