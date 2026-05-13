package bg.nbu.banking_app.data.dto.Loans.ClientLoans;

import bg.nbu.banking_app.data.entity.LoanType;
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
    private BigDecimal amountDisbursed;
    private int paymentTerm;
    private LoanType loanType;
    private BigDecimal currentPayment;
    private LocalDate nextPaymentDate;
}
