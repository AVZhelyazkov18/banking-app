package bg.nbu.banking_app.data.repository;

import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {

    @Query("SELECT pp FROM PaymentPlan pp WHERE pp.loan.id = :loanId")
    List<PaymentPlan> findByLoanId(@Param("loanId") Long loanId);
}
