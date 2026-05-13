package bg.nbu.banking_app.data.dto.Loans.PaymentPlans;

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
}
