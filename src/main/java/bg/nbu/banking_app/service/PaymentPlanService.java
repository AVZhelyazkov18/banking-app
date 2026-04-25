package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;

import java.util.List;

public interface PaymentPlanService {
    List<PaymentPlanDTO> getPaymentPlanFromLoan(long loanId);
}
