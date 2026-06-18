package bg.nbu.banking_app.data.dto.BankAccount;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBankAccountDTO {

    @NotNull(message = "Customer ID is required")
    @Min(value = 1, message = "Customer ID must be a positive number")
    private Long customerId;

    @NotNull(message = "Balance is required")
    @Min(value = 0, message = "Balance must be at least 0")
    private BigDecimal balance;
}
