package bg.nbu.banking_app.data.repository;

import bg.nbu.banking_app.data.entity.PaymentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {
}
