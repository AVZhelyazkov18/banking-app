package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanDTO;
import bg.nbu.banking_app.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    @GetMapping
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

    @GetMapping("/{id}")
    public LoanDTO getLoan(@PathVariable long id) {
        return this.loanService.getLoan(id);
    }

    @PostMapping
    public LoanDTO createLoan(@RequestBody LoanDTO loan) {
        return this.loanService.createLoan(loan);
    }

    @PutMapping("/{id}")
    public LoanDTO updateLoan(@RequestBody LoanDTO loan, @PathVariable long id) {
        return this.loanService.updateLoan(loan, id);
    }

    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable long id) {
        this.loanService.deleteLoan(id);
    }
}
