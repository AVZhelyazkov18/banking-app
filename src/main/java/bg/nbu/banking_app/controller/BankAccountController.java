package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.BankAccount.BankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.UpdateBankAccountDTO;
import bg.nbu.banking_app.service.BankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank-accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public List<BankAccountDTO> getAllBankAccounts() {return bankAccountService.getAllBankAccounts();}

    @GetMapping("/my")
    public List<BankAccountDTO> getMyBankAccounts(Authentication authentication) {
        return bankAccountService.getMyBankAccounts(authentication.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public BankAccountDTO getBankAccount(@PathVariable long id) { return  this.bankAccountService.getBankAccount(id);}

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public BankAccountDTO updateBankAccount(@Valid @RequestBody UpdateBankAccountDTO bankAccount, @PathVariable long id) {
        return this.bankAccountService.updateBankAccount(bankAccount, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBankAccount(@PathVariable long id) {this.bankAccountService.deleteBankAccount(id);}

    //TODO: Add functionality to create a bankAccount by the user and Authorised by Employee and the Admin.
    // Default, when the User creates bankAccount to be Inactive.
}
