package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanDTO;
import bg.nbu.banking_app.data.entity.BankAccount;
import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.repository.BankAccountRepository;
import bg.nbu.banking_app.data.repository.LoanRepository;
import bg.nbu.banking_app.data.repository.PaymentPlanRepository;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private PaymentPlanRepository paymentPlanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private LoanServiceImpl loanService;

    private void mockModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        when(mapperUtil.getModelMapper()).thenReturn(modelMapper);
    }

    private void setId(Object entity, long id) {
        ReflectionTestUtils.setField(entity, "id", id);
    }

    @Test
    void getMyLoansShouldReturnNextUnpaidInstallmentEvenWhenItIsOverdue() {
        mockModelMapper();

        Person customer = new Person();
        setId(customer, 10L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(customer);

        Loan loan = new Loan();
        setId(loan, 100L);
        loan.setCustomer(customer);
        loan.setAmountDisbursed(new BigDecimal("20000.00"));
        loan.setPaymentTerm(18);

        PaymentPlan paidOldInstallment = new PaymentPlan();
        setId(paidOldInstallment, 1L);
        paidOldInstallment.setLoan(loan);
        paidOldInstallment.setPaid(true);
        paidOldInstallment.setPaidDate(LocalDate.now().minusMonths(1));
        paidOldInstallment.setDate(LocalDate.now().minusMonths(1));
        paidOldInstallment.setContributionAmount(new BigDecimal("100.00"));

        PaymentPlan unpaidOverdueInstallment = new PaymentPlan();
        setId(unpaidOverdueInstallment, 2L);
        unpaidOverdueInstallment.setLoan(loan);
        unpaidOverdueInstallment.setPaid(false);
        unpaidOverdueInstallment.setDate(LocalDate.now().minusDays(5));
        unpaidOverdueInstallment.setContributionAmount(new BigDecimal("150.00"));

        PaymentPlan unpaidFutureInstallment = new PaymentPlan();
        setId(unpaidFutureInstallment, 3L);
        unpaidFutureInstallment.setLoan(loan);
        unpaidFutureInstallment.setPaid(false);
        unpaidFutureInstallment.setDate(LocalDate.now().plusMonths(1));
        unpaidFutureInstallment.setContributionAmount(new BigDecimal("200.00"));

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findByCustomerId(10L)).thenReturn(List.of(loan));
        when(paymentPlanRepository.findByLoanId(100L))
                .thenReturn(List.of(paidOldInstallment, unpaidOverdueInstallment, unpaidFutureInstallment));

        List<LoanDTO> result = loanService.getMyLoans("user@test.com");

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("150.00"), result.get(0).getCurrentPayment());
        assertEquals(unpaidOverdueInstallment.getDate(), result.get(0).getNextPaymentDate());

        verify(userRepository).findByEmail("user@test.com");
        verify(loanRepository).findByCustomerId(10L);
        verify(paymentPlanRepository).findByLoanId(100L);
    }

    @Test
    void getMyLoansShouldReturnNullCurrentPaymentWhenLoanIsFullyPaid() {
        mockModelMapper();

        Person customer = new Person();
        setId(customer, 10L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(customer);

        Loan loan = new Loan();
        setId(loan, 100L);
        loan.setCustomer(customer);
        loan.setAmountDisbursed(new BigDecimal("20000.00"));
        loan.setPaymentTerm(18);

        PaymentPlan paidInstallment = new PaymentPlan();
        setId(paidInstallment, 1L);
        paidInstallment.setLoan(loan);
        paidInstallment.setPaid(true);
        paidInstallment.setPaidDate(LocalDate.now());
        paidInstallment.setDate(LocalDate.now());
        paidInstallment.setContributionAmount(new BigDecimal("150.00"));

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findByCustomerId(10L)).thenReturn(List.of(loan));
        when(paymentPlanRepository.findByLoanId(100L)).thenReturn(List.of(paidInstallment));

        List<LoanDTO> result = loanService.getMyLoans("user@test.com");

        assertEquals(1, result.size());
        assertNull(result.get(0).getCurrentPayment());
        assertNull(result.get(0).getNextPaymentDate());
    }

    @Test
    void payNextInstallmentShouldDeductBalanceAndMarkInstallmentAsPaid() {
        mockModelMapper();

        Person customer = new Person();
        setId(customer, 10L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(customer);

        Loan loan = new Loan();
        setId(loan, 100L);
        loan.setCustomer(customer);
        loan.setAmountDisbursed(new BigDecimal("20000.00"));
        loan.setPaymentTerm(18);

        BankAccount bankAccount = new BankAccount();
        setId(bankAccount, 50L);
        bankAccount.setCustomer(customer);
        bankAccount.setBalance(new BigDecimal("500.00"));
        bankAccount.setStatus(true);

        PaymentPlan firstInstallment = new PaymentPlan();
        setId(firstInstallment, 1L);
        firstInstallment.setLoan(loan);
        firstInstallment.setPaid(false);
        firstInstallment.setDate(LocalDate.now());
        firstInstallment.setContributionAmount(new BigDecimal("123.45"));

        PaymentPlan secondInstallment = new PaymentPlan();
        setId(secondInstallment, 2L);
        secondInstallment.setLoan(loan);
        secondInstallment.setPaid(false);
        secondInstallment.setDate(LocalDate.now().plusMonths(1));
        secondInstallment.setContributionAmount(new BigDecimal("200.00"));

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findById(100L)).thenReturn(Optional.of(loan));
        when(bankAccountRepository.findById(50L)).thenReturn(Optional.of(bankAccount));
        when(paymentPlanRepository.findByLoanId(100L))
                .thenReturn(List.of(firstInstallment, secondInstallment));

        LoanDTO result = loanService.payNextInstallment(100L, 50L, "user@test.com");

        assertEquals(new BigDecimal("376.55"), bankAccount.getBalance());
        assertTrue(firstInstallment.isPaid());
        assertEquals(LocalDate.now(), firstInstallment.getPaidDate());

        assertEquals(new BigDecimal("200.00"), result.getCurrentPayment());
        assertEquals(secondInstallment.getDate(), result.getNextPaymentDate());

        verify(bankAccountRepository).save(bankAccount);
        verify(paymentPlanRepository).save(firstInstallment);
    }

    @Test
    void payNextInstallmentShouldThrowWhenBalanceIsInsufficient() {
        Person customer = new Person();
        setId(customer, 10L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(customer);

        Loan loan = new Loan();
        setId(loan, 100L);
        loan.setCustomer(customer);

        BankAccount bankAccount = new BankAccount();
        setId(bankAccount, 50L);
        bankAccount.setCustomer(customer);
        bankAccount.setBalance(new BigDecimal("50.00"));
        bankAccount.setStatus(true);

        PaymentPlan installment = new PaymentPlan();
        setId(installment, 1L);
        installment.setLoan(loan);
        installment.setPaid(false);
        installment.setDate(LocalDate.now());
        installment.setContributionAmount(new BigDecimal("123.45"));

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findById(100L)).thenReturn(Optional.of(loan));
        when(bankAccountRepository.findById(50L)).thenReturn(Optional.of(bankAccount));
        when(paymentPlanRepository.findByLoanId(100L)).thenReturn(List.of(installment));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> loanService.payNextInstallment(100L, 50L, "user@test.com")
        );

        assertEquals("Insufficient funds", exception.getMessage());
        assertFalse(installment.isPaid());
        assertEquals(new BigDecimal("50.00"), bankAccount.getBalance());

        verify(bankAccountRepository, never()).save(any());
        verify(paymentPlanRepository, never()).save(any());
    }

    @Test
    void payNextInstallmentShouldThrowWhenLoanDoesNotBelongToCurrentUser() {
        Person loggedCustomer = new Person();
        setId(loggedCustomer, 10L);

        Person otherCustomer = new Person();
        setId(otherCustomer, 20L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(loggedCustomer);

        Loan loan = new Loan();
        setId(loan, 100L);
        loan.setCustomer(otherCustomer);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findById(100L)).thenReturn(Optional.of(loan));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> loanService.payNextInstallment(100L, 50L, "user@test.com")
        );

        assertEquals("Loan does not belong to the current user", exception.getMessage());

        verify(bankAccountRepository, never()).findById(anyLong());
        verify(bankAccountRepository, never()).save(any());
        verify(paymentPlanRepository, never()).save(any());
    }

    @Test
    void payNextInstallmentShouldThrowWhenBankAccountDoesNotBelongToCurrentUser() {
        Person loggedCustomer = new Person();
        setId(loggedCustomer, 10L);

        Person otherCustomer = new Person();
        setId(otherCustomer, 20L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(loggedCustomer);

        Loan loan = new Loan();
        setId(loan, 100L);
        loan.setCustomer(loggedCustomer);

        BankAccount bankAccount = new BankAccount();
        setId(bankAccount, 50L);
        bankAccount.setCustomer(otherCustomer);
        bankAccount.setBalance(new BigDecimal("500.00"));
        bankAccount.setStatus(true);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findById(100L)).thenReturn(Optional.of(loan));
        when(bankAccountRepository.findById(50L)).thenReturn(Optional.of(bankAccount));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> loanService.payNextInstallment(100L, 50L, "user@test.com")
        );

        assertEquals("Bank account does not belong to the current user", exception.getMessage());

        verify(bankAccountRepository, never()).save(any());
        verify(paymentPlanRepository, never()).save(any());
    }

    @Test
    void payNextInstallmentShouldThrowWhenBankAccountIsInactive() {
        Person customer = new Person();
        setId(customer, 10L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(customer);

        Loan loan = new Loan();
        setId(loan, 100L);
        loan.setCustomer(customer);

        BankAccount bankAccount = new BankAccount();
        setId(bankAccount, 50L);
        bankAccount.setCustomer(customer);
        bankAccount.setBalance(new BigDecimal("500.00"));
        bankAccount.setStatus(false);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findById(100L)).thenReturn(Optional.of(loan));
        when(bankAccountRepository.findById(50L)).thenReturn(Optional.of(bankAccount));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> loanService.payNextInstallment(100L, 50L, "user@test.com")
        );

        assertEquals("Bank account is not active", exception.getMessage());

        verify(bankAccountRepository, never()).save(any());
        verify(paymentPlanRepository, never()).save(any());
    }

    @Test
    void payNextInstallmentShouldThrowWhenLoanIsAlreadyFullyPaid() {
        Person customer = new Person();
        setId(customer, 10L);

        User user = new User();
        user.setEmail("user@test.com");
        user.setCustomer(customer);

        Loan loan = new Loan();
        setId(loan, 100L);
        loan.setCustomer(customer);

        BankAccount bankAccount = new BankAccount();
        setId(bankAccount, 50L);
        bankAccount.setCustomer(customer);
        bankAccount.setBalance(new BigDecimal("500.00"));
        bankAccount.setStatus(true);

        PaymentPlan paidInstallment = new PaymentPlan();
        setId(paidInstallment, 1L);
        paidInstallment.setLoan(loan);
        paidInstallment.setPaid(true);
        paidInstallment.setDate(LocalDate.now());
        paidInstallment.setContributionAmount(new BigDecimal("123.45"));

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(loanRepository.findById(100L)).thenReturn(Optional.of(loan));
        when(bankAccountRepository.findById(50L)).thenReturn(Optional.of(bankAccount));
        when(paymentPlanRepository.findByLoanId(100L)).thenReturn(List.of(paidInstallment));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> loanService.payNextInstallment(100L, 50L, "user@test.com")
        );

        assertEquals("Loan is already fully paid", exception.getMessage());

        verify(bankAccountRepository, never()).save(any());
        verify(paymentPlanRepository, never()).save(any());
    }
}