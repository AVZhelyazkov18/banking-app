package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;

import java.util.List;

public interface PaymentPlanService {
    List<PaymentPlanDTO> getPaymentPlans();
    PaymentPlanDTO getPaymentPlan(long id);
    PaymentPlanDTO createPaymentPlan(PaymentPlanDTO paymentPlan);
    PaymentPlanDTO updatePaymentPlan(PaymentPlanDTO paymentPlan, long id);
    void deletePaymentPlan(long id);
}
