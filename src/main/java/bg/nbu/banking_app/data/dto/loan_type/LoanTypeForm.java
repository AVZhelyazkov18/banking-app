package bg.nbu.banking_app.data.dto.loan_type;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class LoanTypeForm {
    private Long id;

    @NotBlank
    private String credit_name;

    @NotBlank
    private BigDecimal credit_disbursed_min;

    @NotBlank
    private BigDecimal credit_disbursed_max;

    @NotBlank
    private Long credit_term_min;

    @NotBlank
    private Long credit_term_max;

    @NotBlank
    private BigDecimal credit_interest;

}
