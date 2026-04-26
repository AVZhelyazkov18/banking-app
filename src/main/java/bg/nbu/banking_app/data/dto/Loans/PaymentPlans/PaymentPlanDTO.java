package bg.nbu.banking_app.data.dto.Loans.PaymentPlans;

import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
public class PaymentPlanDTO {
    private BigDecimal contributionAmount;
    private BigDecimal principalPortion;
    private BigDecimal interestPortion;
    private LocalDate date;
}
