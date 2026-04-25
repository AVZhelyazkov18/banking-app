package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;
import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import bg.nbu.banking_app.data.repository.LoanRepository;
import bg.nbu.banking_app.data.repository.PaymentPlanRepository;
import bg.nbu.banking_app.service.LoanService;
import bg.nbu.banking_app.service.PaymentPlanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentPlanServiceImpl implements PaymentPlanService {
    private final PaymentPlanRepository paymentPlanRepository;
    private final LoanRepository loanRepository;

    private PaymentPlanDTO mapToDTO(PaymentPlan paymentPlan) {
        return new PaymentPlanDTO(
                paymentPlan.getContributionAmount(),
                paymentPlan.getPrincipalPortion(),
                paymentPlan.getInterestPortion(),
                paymentPlan.getDate()
        );
    }

    private void generatePaymentPlanFromLoan(long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

        if(!loan.getPaymentPlans().isEmpty())
            return;

        BigDecimal loanAmount = loan.getAmountDisbursed();
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(loan.getLoanType().getCreditInterestRate());
        int months = loan.getPaymentTerm();

        BigDecimal monthlyPayment = loanAmount.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        BigDecimal monthlyInterest = monthlyInterestRate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        Set<PaymentPlan> plans = new HashSet<>();

        for (int month = 1; month <= loan.getPaymentTerm(); month++) {
            BigDecimal remainingAmount = loanAmount.subtract(monthlyPayment.multiply(BigDecimal.valueOf(month - 1)));
            BigDecimal interest = remainingAmount.multiply(monthlyInterest).setScale(2, RoundingMode.HALF_UP);

            BigDecimal contributionAmount = monthlyPayment.add(interest);

            PaymentPlan plan = new PaymentPlan();

            plan.setLoan(loan);
            plan.setPrincipalPortion(monthlyPayment);
            plan.setInterestPortion(interest);
            plan.setContributionAmount(contributionAmount);
            plan.setDate(LocalDate.now().plusMonths(month));

            plans.add(plan);
        }

        paymentPlanRepository.saveAll(plans);
    }

    @Override
    @Transactional
    public List<PaymentPlanDTO> getPaymentPlanFromLoan(long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getPaymentPlans().isEmpty())
            generatePaymentPlanFromLoan(loanId);

        return loan.getPaymentPlans().stream()
                .sorted(Comparator.comparing(PaymentPlan::getDate))
                .map(this::mapToDTO)
                .toList();
    }
}
