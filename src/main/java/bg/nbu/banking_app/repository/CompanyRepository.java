package bg.nbu.banking_app.repository;

import bg.nbu.banking_app.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
