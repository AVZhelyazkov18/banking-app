package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;

import java.util.List;
import java.util.Set;

public interface PaymentPlanService {
    public Set<PaymentPlanDTO> getPaymentPlanFromLoan(long loanId);
}
