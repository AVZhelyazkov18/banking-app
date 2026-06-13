package bg.nbu.banking_app.data.repository;

import bg.nbu.banking_app.data.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query("SELECT b FROM BankAccount b WHERE b.customer.id = :customerId")
    List<BankAccount> findByCustomerId(@Param("customerId") Long customerId);
}
