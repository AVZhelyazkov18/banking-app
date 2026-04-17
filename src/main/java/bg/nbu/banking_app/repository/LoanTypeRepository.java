package bg.nbu.banking_app.repository;

import bg.nbu.banking_app.entity.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {
}
