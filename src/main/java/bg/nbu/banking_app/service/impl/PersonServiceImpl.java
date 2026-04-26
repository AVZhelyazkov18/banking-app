package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Customers.NaturalPerson.PersonDTO;
import bg.nbu.banking_app.data.dto.Customers.NaturalPerson.UpdatePersonDTO;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.repository.PersonRepository;
import bg.nbu.banking_app.service.PersonService;
import bg.nbu.banking_app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<PersonDTO> getPeople() {
        return this.mapperUtil.mapList(
                this.personRepository.findAll(), PersonDTO.class);
    }

    @Override
    public PersonDTO getPerson(long id) {
        Person person = this.personRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Person with id " + id + " not found"));

        return this.mapperUtil
                .getModelMapper()
                .map(person, PersonDTO.class);
    }

    @Override
    public PersonDTO createPerson(PersonDTO person) {
        return mapperUtil.getModelMapper()
                .map(this.personRepository
                        .save(mapperUtil.getModelMapper()
                                .map(person, Person.class)), PersonDTO.class);
    }

    @Override
    public PersonDTO updatePerson(UpdatePersonDTO person, long id) {
        Person person1 = this.personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person with id " + id + " not found"));

        person1.setFirstName(person.getFirstName());
        person1.setLastName(person.getLastName());

        Person updated = this.personRepository.save(person1);

        return this.mapperUtil
                .getModelMapper()
                .map(updated, PersonDTO.class);
    }

    @Override
    public void deletePerson(long id) {
        this.personRepository.deleteById(id);
    }
}
