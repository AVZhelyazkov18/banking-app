package bg.nbu.banking_app.data.dto.bank_account;

import bg.nbu.banking_app.data.dto.templates.ObjectResponse;

import java.math.BigDecimal;

public record BankAccountResponse(
        String IBAN,
        BigDecimal balance,
        Boolean status,
        Long customerId
) implements ObjectResponse { }
