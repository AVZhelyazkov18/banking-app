package bg.nbu.banking_app.data.repository;

import bg.nbu.banking_app.data.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByClientNumber(String clientNumber);
    boolean existsByClientNumber(String clientNumber);
}
