package bg.nbu.banking_app.data.dto;

import bg.nbu.banking_app.data.dto.Auth.LoginRequest;
import bg.nbu.banking_app.data.dto.Auth.RegisterRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void loginRequestRejectsInvalidEmailAndBlankPassword() {
        LoginRequest request = new LoginRequest();
        request.setEmail("not-an-email");
        request.setPassword("");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("email", "password");
    }

    @Test
    void registerRequestRequiresCoreClientAndAuthenticationFields() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("client@test.com");
        request.setPassword("secret123");
        request.setRole("ROLE_USER");
        request.setFirstName("Ivan");
        request.setLastName("Petrov");
        request.setPin("9001011234");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }
}
