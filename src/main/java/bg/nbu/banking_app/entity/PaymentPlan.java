package bg.nbu.banking_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class PaymentPlan extends BaseEntity {
    @Column(name = "contribution_amount")
    private BigDecimal contributionAmount;

    @Column(name = "principal_portion")
    private BigDecimal principalPortion;

    @Column(name = "interest_portion")
    private BigDecimal interestPortion;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    private Set<Loan> loans;
}
