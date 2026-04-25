package bg.nbu.banking_app.data.dto.bank_account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import bg.nbu.banking_app.data.dto.templates.ObjectRequest;

import java.math.BigDecimal;

public record BankAccountRequest (

        @NotBlank @Size(max = 34)
        String IBAN,

        BigDecimal balance,

        boolean status
) implements ObjectRequest {}
