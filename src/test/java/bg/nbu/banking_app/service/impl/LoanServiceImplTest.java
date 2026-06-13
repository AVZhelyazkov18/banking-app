package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanDTO;
import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.repository.LoanRepository;
import bg.nbu.banking_app.data.repository.PaymentPlanRepository;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private PaymentPlanRepository paymentPlanRepository;

    @Mock
    private UserRepository userRepository;

    private final MapperUtil mapperUtil = new MapperUtil();

    @Test
    void getMyLoansAddsNearestUpcomingPaymentInformation() {
        Person customer = new Person();
        setField(customer, "id", 15L);

        User user = new User();
        user.setEmail("client@test.com");
        user.setCustomer(customer);

        Loan loan = new Loan();
        setField(loan, "id", 22L);
        loan.setCustomer(customer);
        loan.setAmountDisbursed(new BigDecimal("10000.00"));
        loan.setPaymentTerm(24);

        PaymentPlan pastPayment = new PaymentPlan();
        pastPayment.setContributionAmount(new BigDecimal("400.00"));
        pastPayment.setDate(LocalDate.now().minusMonths(1));

        PaymentPlan firstUpcomingPayment = new PaymentPlan();
        firstUpcomingPayment.setContributionAmount(new BigDecimal("455.25"));
        firstUpcomingPayment.setDate(LocalDate.now().plusMonths(1));

        PaymentPlan laterUpcomingPayment = new PaymentPlan();
        laterUpcomingPayment.setContributionAmount(new BigDecimal("455.25"));
        laterUpcomingPayment.setDate(LocalDate.now().plusMonths(2));

        when(userRepository.findByEmail("client@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findByCustomerId(15L)).thenReturn(List.of(loan));
        when(paymentPlanRepository.findByLoanId(22L)).thenReturn(List.of(
                laterUpcomingPayment,
                pastPayment,
                firstUpcomingPayment
        ));

        LoanServiceImpl service = new LoanServiceImpl(
                loanRepository,
                paymentPlanRepository,
                userRepository,
                mapperUtil
        );

        List<LoanDTO> result = service.getMyLoans("client@test.com");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCurrentPayment()).isEqualByComparingTo("455.25");
        assertThat(result.getFirst().getNextPaymentDate()).isEqualTo(firstUpcomingPayment.getDate());
    }

    @Test
    void getMyLoansReturnsEmptyListWhenUserHasNoCustomer() {
        User user = new User();
        user.setEmail("employee@test.com");

        when(userRepository.findByEmail("employee@test.com")).thenReturn(Optional.of(user));

        LoanServiceImpl service = new LoanServiceImpl(
                loanRepository,
                paymentPlanRepository,
                userRepository,
                mapperUtil
        );

        assertThat(service.getMyLoans("employee@test.com")).isEmpty();
    }

    @Test
    void getMyLoansThrowsWhenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        LoanServiceImpl service = new LoanServiceImpl(
                loanRepository,
                paymentPlanRepository,
                userRepository,
                mapperUtil
        );

        assertThatThrownBy(() -> service.getMyLoans("missing@test.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found: missing@test.com");
    }

    private static void setField(Object target, String fieldName, Object value) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
        throw new IllegalArgumentException("Field not found: " + fieldName);
    }
}
