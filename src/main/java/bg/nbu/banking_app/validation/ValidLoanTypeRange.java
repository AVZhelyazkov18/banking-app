package bg.nbu.banking_app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoanTypeRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLoanTypeRange {
    String message() default "Loan type min values cannot be greater than max values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}