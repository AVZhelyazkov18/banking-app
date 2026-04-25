package bg.nbu.banking_app.data.dto.bank_account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class BankAccountForm {
    private Long id;

    @NotBlank
    @Size(max = 34)
    private String IBAN;

    @NotBlank
    private BigDecimal balance;

    private boolean status;

}
