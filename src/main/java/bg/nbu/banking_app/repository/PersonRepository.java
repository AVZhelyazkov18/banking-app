package bg.nbu.banking_app.repository;

import bg.nbu.banking_app.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
