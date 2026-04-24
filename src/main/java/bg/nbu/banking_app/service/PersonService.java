package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.PersonDTO;
import bg.nbu.banking_app.data.dto.UpdatePersonDTO;

import java.util.List;

public interface PersonService {
    List<PersonDTO> getPeople();
    PersonDTO getPerson(long id);
    PersonDTO createPerson(PersonDTO person);
    PersonDTO updatePerson(UpdatePersonDTO person, long id);
    void deletePerson(long id);
}
