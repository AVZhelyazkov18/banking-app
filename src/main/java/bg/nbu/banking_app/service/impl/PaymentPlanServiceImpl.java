package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;
import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import bg.nbu.banking_app.data.repository.LoanRepository;
import bg.nbu.banking_app.data.repository.PaymentPlanRepository;
import bg.nbu.banking_app.service.PaymentPlanService;
import bg.nbu.banking_app.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentPlanServiceImpl implements PaymentPlanService {
    private final PaymentPlanRepository paymentPlanRepository;
    private final MapperUtil mapperUtil;
    private final LoanRepository loanRepository;

    @Override
    public List<PaymentPlanDTO> getPaymentPlans() {
        return this.mapperUtil.mapList(
                this.paymentPlanRepository.findAll(), PaymentPlanDTO.class);
    }

    @Override
    public PaymentPlanDTO getPaymentPlan(long id) {
        PaymentPlan paymentPlan = this.paymentPlanRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentPlan with id " + id + " not found"));

        return this.mapperUtil
                .getModelMapper()
                .map(paymentPlan, PaymentPlanDTO.class);
    }

    @Override
    public PaymentPlanDTO createPaymentPlan(PaymentPlanDTO paymentPlan) {
        return mapperUtil.getModelMapper()
                .map(this.paymentPlanRepository
                        .save(mapperUtil.getModelMapper()
                                .map(paymentPlan, PaymentPlan.class)), PaymentPlanDTO.class);
    }



    @Override
    public PaymentPlanDTO updatePaymentPlan(PaymentPlanDTO paymentPlan, long id) {
        PaymentPlan paymentPlan1 = this.paymentPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentPlan with id " + id + " not found"));

        paymentPlan1.setDate(paymentPlan.getDate());
        paymentPlan1.setInterestPortion(paymentPlan.getInterestPortion());
        paymentPlan1.setPrincipalPortion(paymentPlan.getPrincipalPortion());
        paymentPlan1.setContributionAmount(paymentPlan.getContributionAmount());

        PaymentPlan updated = this.paymentPlanRepository.save(paymentPlan1);

        return this.mapperUtil
                .getModelMapper()
                .map(updated, PaymentPlanDTO.class);
    }

    @Override
    public void deletePaymentPlan(long id) {
        this.paymentPlanRepository.deleteById(id);
    }

    @Override
    public PaymentPlanDTO markInstallmentAsPaid(long id) {
        return markInstallmentAsPaid(id, LocalDate.now());
    }

    @Override
    public PaymentPlanDTO markInstallmentAsPaid(long id, LocalDate paidDate) {
        if (paidDate == null) {
            throw new IllegalArgumentException("Payment date is required");
        }

        PaymentPlan paymentPlan = this.paymentPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentPlan with id " + id + " not found"));

        if (paymentPlan.isPaid()) {
            throw new IllegalStateException("PaymentPlan with id " + id + " is already paid");
        }

        paymentPlan.setPaid(true);
        paymentPlan.setPaidDate(paidDate);

        PaymentPlan updated = this.paymentPlanRepository.save(paymentPlan);

        return this.mapperUtil
                .getModelMapper()
                .map(updated, PaymentPlanDTO.class);
    }


    @Override
    @Transactional
    public List<PaymentPlanDTO> getPaymentPlanFromLoan(long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        List<PaymentPlan> plans = loan.getPaymentPlans() == null
                ? new ArrayList<>()
                : new ArrayList<>(loan.getPaymentPlans());

        if (plans.isEmpty()) {
            BigDecimal loanAmount = loan.getAmountDisbursed();
            int months = loan.getPaymentTerm();

            if (loanAmount == null || loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Loan amount must be greater than 0");
            }

            if (months <= 0) {
                throw new IllegalArgumentException("Payment term must be greater than 0");
            }

            BigDecimal annualInterestRatePercent = BigDecimal.valueOf(
                    loan.getLoanType().getCreditInterestRate()
            );

            if (annualInterestRatePercent.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Interest rate cannot be negative");
            }

            BigDecimal monthlyInterestRate = annualInterestRatePercent
                    .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

            BigDecimal monthlyContributionAmount;

            if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
                monthlyContributionAmount = loanAmount
                        .divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
            } else {
                double r = monthlyInterestRate.doubleValue();
                double factor = Math.pow(1 + r, months);

                double annuityCoefficient = (r * factor) / (factor - 1);

                monthlyContributionAmount = loanAmount
                        .multiply(BigDecimal.valueOf(annuityCoefficient))
                        .setScale(2, RoundingMode.HALF_UP);
            }

            BigDecimal remainingAmount = loanAmount.setScale(2, RoundingMode.HALF_UP);

            for (int month = 1; month <= months; month++) {
                BigDecimal interestPortion = remainingAmount
                        .multiply(monthlyInterestRate)
                        .setScale(2, RoundingMode.HALF_UP);

                BigDecimal principalPortion = monthlyContributionAmount
                        .subtract(interestPortion)
                        .setScale(2, RoundingMode.HALF_UP);

                BigDecimal contributionAmount = monthlyContributionAmount;

                if (month == months) {
                    principalPortion = remainingAmount;
                    contributionAmount = principalPortion
                            .add(interestPortion)
                            .setScale(2, RoundingMode.HALF_UP);
                }

                remainingAmount = remainingAmount
                        .subtract(principalPortion)
                        .setScale(2, RoundingMode.HALF_UP);

                PaymentPlan plan = new PaymentPlan();

                plan.setLoan(loan);
                plan.setPrincipalPortion(principalPortion);
                plan.setInterestPortion(interestPortion);
                plan.setContributionAmount(contributionAmount);
                plan.setDate(LocalDate.now().plusMonths(month));
                plan.setPaid(false);
                plan.setPaidDate(null);

                plans.add(plan);
            }

            plans = paymentPlanRepository.saveAll(plans);
        }

        return plans.stream()
                .sorted(Comparator.comparing(PaymentPlan::getDate))
                .map(plan -> mapperUtil.getModelMapper().map(plan, PaymentPlanDTO.class))
                .toList();
    }
}
