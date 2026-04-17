package bg.nbu.banking_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.Set;

public class Loan extends BaseEntity {
    @Column(name = "amount_disburesd")
    private BigDecimal amountDisbursed;

    @Column(name = "payment_term")
    private int paymentTerm;

    @ManyToOne
    private LoanType loanType;

    @ManyToOne
    private Customer customer;

    @OneToMany
    private Set<PaymentPlan> paymentPlans;
}
