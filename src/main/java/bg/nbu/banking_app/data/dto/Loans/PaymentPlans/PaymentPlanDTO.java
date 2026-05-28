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
    private Long id;
    private BigDecimal contributionAmount;
    private BigDecimal principalPortion;
    private BigDecimal interestPortion;
    private LocalDate date;
    private boolean paid;
    private LocalDate paidDate;

    // This constructor is used when paid and paidDate are not provided when defining DTO
    public PaymentPlanDTO(
            Long id,
            BigDecimal contributionAmount,
            BigDecimal principalPortion,
            BigDecimal interestPortion,
            LocalDate date
    ) {
        this.id = id;
        this.contributionAmount = contributionAmount;
        this.principalPortion = principalPortion;
        this.interestPortion = interestPortion;
        this.date = date;
        this.paid = false;
        this.paidDate = null;
    }

}

