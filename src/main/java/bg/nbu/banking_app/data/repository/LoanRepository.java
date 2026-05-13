package bg.nbu.banking_app.data.repository;

import bg.nbu.banking_app.data.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l FROM Loan l WHERE l.customer.id = :customerId")
    List<Loan> findByCustomerId(@Param("customerId") Long customerId);
}
