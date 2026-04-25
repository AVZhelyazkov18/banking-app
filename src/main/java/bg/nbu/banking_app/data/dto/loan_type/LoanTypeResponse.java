package bg.nbu.banking_app.data.dto.loan_type;

import bg.nbu.banking_app.data.dto.templates.ObjectResponse;

import java.math.BigDecimal;

public record LoanTypeResponse(
        String credit_name,

        BigDecimal credit_disbursed_min,

        BigDecimal credit_disbursed_max,

        Long credit_term_min,

        Long credit_term_max,

        BigDecimal credit_interest
) implements ObjectResponse {
}
