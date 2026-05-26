package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import bg.nbu.banking_app.data.repository.PaymentPlanRepository;
import bg.nbu.banking_app.service.PaymentPlanService;
import bg.nbu.banking_app.util.MapperUtil;
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
    private final MapperUtil mapperUtil;

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
    public List<PaymentPlanDTO> generateAnnuityPaymentPlan(
            BigDecimal amountDisbursed,
            double annualInterestRatePercent,
            int paymentTermMonths,
            LocalDate firstPaymentDate
    ) {
        if (amountDisbursed == null || amountDisbursed.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if (annualInterestRatePercent < 0) {
            throw new IllegalArgumentException("Annual interest rate cannot be negative");
        }
        if (paymentTermMonths <= 0) {
            throw new IllegalArgumentException("Payment term must be positive");
        }
        if (firstPaymentDate == null) {
            throw new IllegalArgumentException("First payment date is required");
        }

        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRatePercent)
                .divide(BigDecimal.valueOf(100 * 12), 10, RoundingMode.HALF_UP);

        BigDecimal monthlyPayment;
        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            monthlyPayment = amountDisbursed.divide(
                    BigDecimal.valueOf(paymentTermMonths), 2, RoundingMode.HALF_UP);
        } else {
            double rate = annualInterestRatePercent / 100.0 / 12.0;
            double annuityCoefficient = rate / (1 - Math.pow(1 + rate, -paymentTermMonths));
            monthlyPayment = BigDecimal.valueOf(amountDisbursed.doubleValue() * annuityCoefficient)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal remainingPrincipal = amountDisbursed.setScale(2, RoundingMode.HALF_UP);
        List<PaymentPlanDTO> paymentPlan = new ArrayList<>();

        for (int month = 1; month <= paymentTermMonths; month++) {
            BigDecimal interestPortion = remainingPrincipal
                    .multiply(monthlyInterestRate)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPortion = monthlyPayment
                    .subtract(interestPortion)
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal contributionAmount = monthlyPayment;

            if (month == paymentTermMonths) {
                principalPortion = remainingPrincipal;
                contributionAmount = principalPortion.add(interestPortion).setScale(2, RoundingMode.HALF_UP);
            }

            PaymentPlanDTO monthlyInstallment = new PaymentPlanDTO(
                    null,
                    contributionAmount,
                    principalPortion,
                    interestPortion,
                    firstPaymentDate.plusMonths(month - 1L)
            );
            paymentPlan.add(monthlyInstallment);

            remainingPrincipal = remainingPrincipal
                    .subtract(principalPortion)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return paymentPlan;
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
}
