package bg.nbu.banking_app.data.dto.Loans.ClientLoans;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanPaymentRequest {

    @NotNull(message = "bankAccountId is required")
    @Positive(message = "bankAccountId must be greater than 0")
    private Long bankAccountId;
}