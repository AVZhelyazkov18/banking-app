package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Customers.NaturalPerson.PersonDTO;
import bg.nbu.banking_app.data.dto.Customers.NaturalPerson.UpdatePersonDTO;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.repository.PersonRepository;
import bg.nbu.banking_app.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    private final MapperUtil mapperUtil = new MapperUtil();

    @Test
    void createPersonSavesMappedEntityAndReturnsDto() {
        PersonDTO request = new PersonDTO("Ivan", "Petrov", "9001011234");

        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PersonServiceImpl service = new PersonServiceImpl(personRepository, mapperUtil);

        PersonDTO result = service.createPerson(request);

        assertThat(result.getFirstName()).isEqualTo("Ivan");
        assertThat(result.getLastName()).isEqualTo("Petrov");
        assertThat(result.getPin()).isEqualTo("9001011234");

        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(personCaptor.capture());
        assertThat(personCaptor.getValue().getFirstName()).isEqualTo("Ivan");
    }

    @Test
    void updatePersonChangesOnlyEditableFields() {
        Person existing = new Person();
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setPin("9001011234");

        UpdatePersonDTO update = new UpdatePersonDTO("New", "Person");

        when(personRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(personRepository.save(existing)).thenReturn(existing);

        PersonServiceImpl service = new PersonServiceImpl(personRepository, mapperUtil);

        PersonDTO result = service.updatePerson(update, 1L);

        assertThat(result.getFirstName()).isEqualTo("New");
        assertThat(result.getLastName()).isEqualTo("Person");
        assertThat(result.getPin()).isEqualTo("9001011234");
        verify(personRepository).save(existing);
    }

    @Test
    void getPersonThrowsWhenPersonDoesNotExist() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        PersonServiceImpl service = new PersonServiceImpl(personRepository, mapperUtil);

        assertThatThrownBy(() -> service.getPerson(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Person with id 99 not found");
    }
}
