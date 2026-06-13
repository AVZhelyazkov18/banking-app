package bg.nbu.banking_app.data.dto.Loans.PaymentPlans;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotNull(message = "Contribution amount is required")
    @PositiveOrZero(message = "contributionAmount must be greater than 0")
    private BigDecimal contributionAmount;

    @NotNull(message = "principalPortion amount is required")
    @PositiveOrZero(message = "principalPortion must be greater than 0")
    private BigDecimal principalPortion;

    @NotNull(message = "interestPortion amount is required")
    @PositiveOrZero(message = "interestPortion must be greater than 0")
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

