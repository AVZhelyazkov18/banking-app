package bg.nbu.banking_app.data.repository;

import bg.nbu.banking_app.data.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void findByClientNumberReturnsMatchingPerson() {
        Person person = new Person();
        person.setFirstName("Ivan");
        person.setLastName("Petrov");
        person.setPin("9001011234");
        person.setClientNumber("CL00000001");

        entityManager.persistAndFlush(person);

        assertThat(personRepository.findByClientNumber("CL00000001"))
                .isPresent()
                .get()
                .extracting(Person::getFirstName)
                .isEqualTo("Ivan");
    }

    @Test
    void existsByClientNumberReturnsFalseForMissingClientNumber() {
        assertThat(personRepository.existsByClientNumber("MISSING")).isFalse();
    }
}
