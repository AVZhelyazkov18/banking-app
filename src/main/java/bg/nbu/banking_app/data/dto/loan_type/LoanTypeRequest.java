package bg.nbu.banking_app.data.dto.loan_type;

import bg.nbu.banking_app.data.dto.templates.ObjectRequest;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record LoanTypeRequest(
        @NotBlank
        String credit_name,

        @NotBlank
        BigDecimal credit_disbursed_min,

        @NotBlank
        BigDecimal credit_disbursed_max,

        @NotBlank
        Long credit_term_min,

        @NotBlank
        Long credit_term_max,

        @NotBlank
        BigDecimal credit_interest
) implements ObjectRequest {
}
