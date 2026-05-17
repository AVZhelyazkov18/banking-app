package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;
import bg.nbu.banking_app.service.PaymentPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment_plans")
public class PaymentPlanController {
    private final PaymentPlanService paymentPlanService;

    @GetMapping("/{loanId}")
    public Set<PaymentPlanDTO> getPaymentPlan(@PathVariable long loanId) {
        return this.paymentPlanService.getPaymentPlanFromLoan(loanId);
    }
}
