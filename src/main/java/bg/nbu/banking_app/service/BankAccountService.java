package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.BankAccount.BankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.CreateBankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.UpdateBankAccountDTO;

import java.util.List;

public interface BankAccountService {
    List<BankAccountDTO> getAllBankAccounts();
    BankAccountDTO getBankAccount(long id);
    BankAccountDTO createBankAccount(CreateBankAccountDTO dto);
    BankAccountDTO updateBankAccount(UpdateBankAccountDTO company, long id);
    void deleteBankAccount(long id);
    List<BankAccountDTO> getMyBankAccounts(String username);
}
