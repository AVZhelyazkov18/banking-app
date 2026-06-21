package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanDTO;
import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanPaymentRequest;
import bg.nbu.banking_app.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

    @GetMapping("/my")
    public List<LoanDTO> getMyLoans(Authentication authentication) {
        return loanService.getMyLoans(authentication.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public LoanDTO getLoan(@PathVariable long id) {
        return this.loanService.getLoan(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public LoanDTO createLoan(@Valid @RequestBody LoanDTO loan) {
        return this.loanService.createLoan(loan);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public LoanDTO updateLoan(@Valid @RequestBody LoanDTO loan, @PathVariable long id) {
        return this.loanService.updateLoan(loan, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLoan(@PathVariable long id) {
        this.loanService.deleteLoan(id);
    }

    @PostMapping("/{id}/pay")
    public LoanDTO payNextInstallment(
            @PathVariable long id,
            @Valid @RequestBody LoanPaymentRequest request,
            Authentication authentication
    ) {
        return loanService.payNextInstallment(
                id,
                request.getBankAccountId(),
                authentication.getName()
        );
    }
}
