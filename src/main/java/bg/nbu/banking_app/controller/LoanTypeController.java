package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Loans.LoanTypes.LoanTypeDTO;
import bg.nbu.banking_app.service.LoanTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loan_types")
public class LoanTypeController {
    private final LoanTypeService loanTypeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public List<LoanTypeDTO> getLoanTypes() {
        return loanTypeService.getLoanTypes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public LoanTypeDTO getLoanType(@PathVariable long id) {
        return this.loanTypeService.getLoanType(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public LoanTypeDTO createLoanType(@Valid @RequestBody LoanTypeDTO loanType) {
        return this.loanTypeService.createLoanType(loanType);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public LoanTypeDTO updateLoanType(@Valid @RequestBody LoanTypeDTO loanType, @PathVariable long id) {
        return this.loanTypeService.updateLoanType(loanType, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLoanType(@PathVariable long id) {
        this.loanTypeService.deleteLoanType(id);
    }

    //TODO: Creade, Update, Delete work for new LoanTypes. Check the pre-created test data. It does not work there
}
