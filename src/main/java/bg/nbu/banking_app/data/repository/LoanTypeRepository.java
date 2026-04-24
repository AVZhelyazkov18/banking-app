package bg.nbu.banking_app.data.repository;

import bg.nbu.banking_app.data.entity.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanTypeRepository extends JpaRepository<LoanType, Long> {
}
