package bg.nbu.banking_app.validation;

import bg.nbu.banking_app.data.dto.Loans.LoanTypes.LoanTypeDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoanTypeRangeValidator implements ConstraintValidator<ValidLoanTypeRange, LoanTypeDTO> {

    @Override
    public boolean isValid(LoanTypeDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        boolean valid = true;

        context.disableDefaultConstraintViolation();

        if (dto.getCreditDisbursedMin() > dto.getCreditDisbursedMax()) {
            context.buildConstraintViolationWithTemplate("creditDisbursedMin cannot be greater than creditDisbursedMax")
                    .addPropertyNode("creditDisbursedMin")
                    .addConstraintViolation();
            valid = false;
        }else if (dto.getCreditTermMin() > dto.getCreditTermMax()) {
            context.buildConstraintViolationWithTemplate("creditTermMin cannot be greater than creditTermMax")
                    .addPropertyNode("creditTermMin")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}