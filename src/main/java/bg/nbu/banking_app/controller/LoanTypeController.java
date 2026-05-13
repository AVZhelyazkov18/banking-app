package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Loans.LoanTypes.LoanTypeDTO;
import bg.nbu.banking_app.service.LoanTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loan_types")
public class LoanTypeController {
    private final LoanTypeService loanTypeService;

    @GetMapping
    public List<LoanTypeDTO> getLoanTypes() {
        return loanTypeService.getLoanTypes();
    }

    @GetMapping("/{id}")
    public LoanTypeDTO getLoanType(@PathVariable long id) {
        return this.loanTypeService.getLoanType(id);
    }

    @PostMapping
    public LoanTypeDTO createLoanType(@RequestBody LoanTypeDTO loanType) {
        return this.loanTypeService.createLoanType(loanType);
    }

    @PutMapping("/{id}")
    public LoanTypeDTO updateLoanType(@RequestBody LoanTypeDTO loanType, @PathVariable long id) {
        return this.loanTypeService.updateLoanType(loanType, id);
    }

    @DeleteMapping("/{id}")
    public void deleteLoanType(@PathVariable long id) {
        this.loanTypeService.deleteLoanType(id);
    }
}
