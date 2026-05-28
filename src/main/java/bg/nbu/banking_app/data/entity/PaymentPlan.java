package bg.nbu.banking_app.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
public class PaymentPlan extends BaseEntity {
    @Column(name = "contribution_amount")
    private BigDecimal contributionAmount;

    @Column(name = "principal_portion")
    private BigDecimal principalPortion;

    @Column(name = "interest_portion")
    private BigDecimal interestPortion;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @Column(name = "paid", nullable = false)
    private boolean paid = false;

    @Column(name = "paid_date")
    private LocalDate paidDate;
}
