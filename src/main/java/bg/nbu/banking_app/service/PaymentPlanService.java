package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentPlanService {
    List<PaymentPlanDTO> getPaymentPlans();
    PaymentPlanDTO getPaymentPlan(long id);
    PaymentPlanDTO createPaymentPlan(PaymentPlanDTO paymentPlan);
    PaymentPlanDTO updatePaymentPlan(PaymentPlanDTO paymentPlan, long id);
    void deletePaymentPlan(long id);

    List<PaymentPlanDTO> getPaymentPlanFromLoan(long loanId);

    List<PaymentPlanDTO> generateAnnuityPaymentPlan(
            BigDecimal amountDisbursed,
            double annualInterestRatePercent,
            int paymentTermMonths,
            LocalDate firstPaymentDate
    );

    PaymentPlanDTO markInstallmentAsPaid(long id);
    PaymentPlanDTO markInstallmentAsPaid(long id, LocalDate paidDate);
}
