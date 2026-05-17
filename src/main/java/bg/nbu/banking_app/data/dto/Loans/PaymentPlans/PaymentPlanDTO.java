package bg.nbu.banking_app.data.dto.Loans.PaymentPlans;

import bg.nbu.banking_app.data.entity.PaymentPlan;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentPlanDTO {
    private BigDecimal contributionAmount;
    private BigDecimal principalPortion;
    private BigDecimal interestPortion;
    private LocalDate date;


    public static PaymentPlanDTO mapToDTO(PaymentPlan paymentPlan) {
        return new PaymentPlanDTO(
                paymentPlan.getContributionAmount(),
                paymentPlan.getPrincipalPortion(),
                paymentPlan.getInterestPortion(),
                paymentPlan.getDate()
        );
    }
}
