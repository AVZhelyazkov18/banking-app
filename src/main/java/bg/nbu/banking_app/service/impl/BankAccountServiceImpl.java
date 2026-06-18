package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.BankAccount.BankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.CreateBankAccountDTO;
import bg.nbu.banking_app.data.dto.BankAccount.UpdateBankAccountDTO;
import bg.nbu.banking_app.data.entity.BankAccount;
import bg.nbu.banking_app.data.entity.Customer;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.repository.BankAccountRepository;
import bg.nbu.banking_app.data.repository.CustomerRepository;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.service.BankAccountService;
import bg.nbu.banking_app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
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
    public BankAccountDTO createBankAccount(CreateBankAccountDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer with id " + dto.getCustomerId() + " not found"));

        String iban = "BG" + UUID.randomUUID().toString().replace("-", "").substring(0, 18).toUpperCase();

        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban(iban);
        bankAccount.setBalance(dto.getBalance());
        bankAccount.setStatus(false);
        bankAccount.setCustomer(customer);

        return mapperUtil.getModelMapper()
                .map(bankAccountRepository.save(bankAccount), BankAccountDTO.class);
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

    @Override
    public List<BankAccountDTO> getMyBankAccounts(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        if (user.getCustomer() == null) return List.of();
        return this.mapperUtil.mapList(
                bankAccountRepository.findByCustomerId(user.getCustomer().getId()),
                BankAccountDTO.class
        );
    }
}
