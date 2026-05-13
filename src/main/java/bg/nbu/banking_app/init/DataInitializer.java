package bg.nbu.banking_app.init;

import bg.nbu.banking_app.data.entity.*;
import bg.nbu.banking_app.data.enums.Role;
import bg.nbu.banking_app.data.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final BankAccountRepository bankAccountRepository;
    private final LoanRepository loanRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        // --- Loan Types ---
        LoanType personalLoan = new LoanType();
        personalLoan.setCreditName("Personal Loan");
        personalLoan.setCreditDisbursedMin(1000);
        personalLoan.setCreditDisbursedMax(50000);
        personalLoan.setCreditTermMin(6);
        personalLoan.setCreditTermMax(60);
        personalLoan.setCreditInterestRate(8.5);
        loanTypeRepository.save(personalLoan);

        LoanType mortgage = new LoanType();
        mortgage.setCreditName("Mortgage");
        mortgage.setCreditDisbursedMin(10000);
        mortgage.setCreditDisbursedMax(500000);
        mortgage.setCreditTermMin(12);
        mortgage.setCreditTermMax(360);
        mortgage.setCreditInterestRate(3.5);
        loanTypeRepository.save(mortgage);

        // --- Person Customers ---
        Person ivan = new Person();
        ivan.setFirstName("Ivan");
        ivan.setLastName("Petrov");
        setField(ivan, "pin", "9001011234");
        personRepository.save(ivan);

        Person maria = new Person();
        maria.setFirstName("Maria");
        maria.setLastName("Georgieva");
        setField(maria, "pin", "8505155678");
        personRepository.save(maria);

        // --- Company Customer ---
        Company techCorp = new Company();
        techCorp.setCompanyName("TechCorp EOOD");
        setField(techCorp, "eik", "123456789");
        techCorp.setRepresentative("Georgi Ivanov");
        companyRepository.save(techCorp);

        // --- Bank Accounts ---
        BankAccount ivanAccount = new BankAccount();
        setField(ivanAccount, "iban", "BG80BNBG96611020345678");
        ivanAccount.setBalance(BigDecimal.valueOf(55.99));
        ivanAccount.setStatus(true);
        setField(ivanAccount, "customer", ivan);
        bankAccountRepository.save(ivanAccount);

        BankAccount ivanAccount2 = new BankAccount();
        setField(ivanAccount2, "iban", "BG11BNBG22221234567890");
        ivanAccount2.setBalance(BigDecimal.valueOf(3200.00));
        ivanAccount2.setStatus(false);
        setField(ivanAccount2, "customer", ivan);
        bankAccountRepository.save(ivanAccount2);

        BankAccount mariaAccount = new BankAccount();
        setField(mariaAccount, "iban", "BG18RZBB91550123456789");
        mariaAccount.setBalance(BigDecimal.valueOf(12500.00));
        mariaAccount.setStatus(true);
        setField(mariaAccount, "customer", maria);
        bankAccountRepository.save(mariaAccount);

        BankAccount corpAccount = new BankAccount();
        setField(corpAccount, "iban", "BG29NWBK72030140488201");
        corpAccount.setBalance(BigDecimal.valueOf(250000.00));
        corpAccount.setStatus(true);
        setField(corpAccount, "customer", techCorp);
        bankAccountRepository.save(corpAccount);

        // --- Loans for Ivan ---

        // Loan 1: active — next payment due next month
        Loan loan1 = new Loan();
        loan1.setAmountDisbursed(BigDecimal.valueOf(50000));
        loan1.setPaymentTerm(60);
        loan1.setLoanType(personalLoan);
        loan1.setCustomer(ivan);
        loan1 = loanRepository.save(loan1);

        PaymentPlan plan1 = new PaymentPlan();
        plan1.setContributionAmount(BigDecimal.valueOf(456.00));
        plan1.setPrincipalPortion(BigDecimal.valueOf(350.00));
        plan1.setInterestPortion(BigDecimal.valueOf(106.00));
        plan1.setDate(LocalDate.now().plusMonths(1));
        plan1.setLoan(loan1);
        paymentPlanRepository.save(plan1);

        // Loan 2: fully paid — last payment was in the past
        Loan loan2 = new Loan();
        loan2.setAmountDisbursed(BigDecimal.valueOf(100000));
        loan2.setPaymentTerm(24);
        loan2.setLoanType(mortgage);
        loan2.setCustomer(ivan);
        loan2 = loanRepository.save(loan2);

        PaymentPlan plan2 = new PaymentPlan();
        plan2.setContributionAmount(BigDecimal.valueOf(4600.00));
        plan2.setPrincipalPortion(BigDecimal.valueOf(4300.00));
        plan2.setInterestPortion(BigDecimal.valueOf(300.00));
        plan2.setDate(LocalDate.of(2026, 4, 4));
        plan2.setLoan(loan2);
        paymentPlanRepository.save(plan2);

        // --- Users ---
        User admin = User.builder()
                .email("admin@bank.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ROLE_ADMIN)
                .build();
        userRepository.save(admin);

        User ivanUser = User.builder()
                .email("ivan@bank.com")
                .password(passwordEncoder.encode("user123"))
                .role(Role.ROLE_USER)
                .customer(ivan)
                .build();
        userRepository.save(ivanUser);

        User mariaUser = User.builder()
                .email("maria@bank.com")
                .password(passwordEncoder.encode("user123"))
                .role(Role.ROLE_USER)
                .customer(maria)
                .build();
        userRepository.save(mariaUser);
    }

    private void setField(Object target, String fieldName, Object value) {
        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, target, value);
    }
}
