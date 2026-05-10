package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.BankAccount.BankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.UpdateBankAccountDTO;
import bg.nbu.banking_app.data.dto.Customers.Company.CompanyDTO;
import bg.nbu.banking_app.data.entity.BankAccount;
import bg.nbu.banking_app.data.entity.Company;
import bg.nbu.banking_app.data.repository.BankAccountRepository;
import bg.nbu.banking_app.service.BankAccountService;
import bg.nbu.banking_app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<BankAccountDTO> getAllBankAccounts(){
        return this.mapperUtil.mapList(
                this.bankAccountRepository.findAll(), BankAccountDTO.class
        );
    }

    @Override
    public BankAccountDTO getBankAccount(long id) {
        BankAccount bankAccount = this.bankAccountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Bank Account with id " + id + " not found and cannot be read"));

        return this.mapperUtil
                .getModelMapper()
                .map(bankAccount, BankAccountDTO.class);
    }

    @Override
    public BankAccountDTO createBankAccount(BankAccountDTO bankAccount) {
        return mapperUtil.getModelMapper()
                .map(this.bankAccountRepository
                        .save(mapperUtil.getModelMapper()
                                .map(bankAccount, BankAccount.class)), BankAccountDTO.class);
    }

    @Override
    public BankAccountDTO updateBankAccount(UpdateBankAccountDTO bankAccount, long id) {
        BankAccount bankAccountCurrent = this.bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank account with id " + id + " not found and cannot be updated"));

        bankAccountCurrent.setBalance(bankAccount.getBalance());
        bankAccountCurrent.setStatus(bankAccount.isStatus());

        BankAccount updated = this.bankAccountRepository.save(bankAccountCurrent);

        return this.mapperUtil
                .getModelMapper()
                .map(updated, BankAccountDTO.class);
    }

    //See if this is even needed
    @Override
    public void deleteBankAccount(long id) { this.bankAccountRepository.deleteById(id);}
}
