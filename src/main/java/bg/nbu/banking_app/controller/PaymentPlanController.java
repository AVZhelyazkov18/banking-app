package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;
import bg.nbu.banking_app.service.PaymentPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment_plans")
public class PaymentPlanController {
    private final PaymentPlanService paymentPlanService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public List<PaymentPlanDTO> getPaymentPlans() {
        return paymentPlanService.getPaymentPlans();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PaymentPlanDTO getPaymentPlan(@PathVariable long id) {
        return this.paymentPlanService.getPaymentPlan(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PaymentPlanDTO createPaymentPlan(@Valid @RequestBody PaymentPlanDTO paymentPlan) {
        return this.paymentPlanService.createPaymentPlan(paymentPlan); //TODO: Check passing ID inside the RequestBody. Maybe the Backend will need to add it and not to be sned from FE
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PaymentPlanDTO updatePaymentPlan(@Valid @RequestBody PaymentPlanDTO paymentPlan, @PathVariable long id) {
        return this.paymentPlanService.updatePaymentPlan(paymentPlan, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePaymentPlan(@PathVariable long id) {
        this.paymentPlanService.deletePaymentPlan(id);
    }

    @PatchMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PaymentPlanDTO markInstallmentAsPaid(@PathVariable long id) {
        return this.paymentPlanService.markInstallmentAsPaid(id);
    }
}
