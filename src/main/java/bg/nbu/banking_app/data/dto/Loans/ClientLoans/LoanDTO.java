package bg.nbu.banking_app.data.dto.Loans.ClientLoans;

import bg.nbu.banking_app.data.entity.Customer;
import bg.nbu.banking_app.data.entity.LoanType;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateLoanDTO {
    private BigDecimal amountDisbursed;
    private int paymentTerm;
    private long loanTypeId;
    private long customerId;
}
