package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.BankAccount.BankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.UpdateBankAccountDTO;
import bg.nbu.banking_app.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank-accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @GetMapping
    public List<BankAccountDTO> getAllBankAccounts() {return bankAccountService.getAllBankAccounts();}

    @GetMapping("/{id}")
    public BankAccountDTO getBankAccount(@PathVariable long id) { return  this.bankAccountService.getBankAccount(id);}

    @PostMapping("/{id}")
    public BankAccountDTO updateBankAccount(@RequestBody UpdateBankAccountDTO bankAccount, @PathVariable long id) {
        return this.bankAccountService.updateBankAccount(bankAccount, id);
    }

    // Do we need to be able to delete bank accounts?
    @DeleteMapping("/{id}")
    public void deleteBankAccount(@PathVariable long id) {this.bankAccountService.deleteBankAccount(id);}
}
