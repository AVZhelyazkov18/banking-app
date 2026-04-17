package bg.nbu.banking_app.repository;

import bg.nbu.banking_app.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
