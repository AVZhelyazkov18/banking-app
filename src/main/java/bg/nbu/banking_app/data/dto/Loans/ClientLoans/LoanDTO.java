package bg.nbu.banking_app.data.dto.Loans.ClientLoans;

import bg.nbu.banking_app.data.entity.LoanType;
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
public class LoanDTO {
    private Long id;
    @PositiveOrZero(message = "amountDisbursed must be greater than 0")
    private BigDecimal amountDisbursed;
    @PositiveOrZero(message = "paymentTerm must be greater than 0")
    private int paymentTerm;
    private LoanType loanType;
    @PositiveOrZero(message = "currentPayment must be greater than 0")
    private BigDecimal currentPayment;
    private LocalDate nextPaymentDate;

    @NotNull(message = "customerId is required")
    @Positive(message = "customerId must be greater than 0")
    private Long customerId;
}
