package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.PersonDTO;
import bg.nbu.banking_app.data.dto.UpdatePersonDTO;
import bg.nbu.banking_app.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/people")
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public List<PersonDTO> getPeople() {
        return personService.getPeople();
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable long id) {
        return this.personService.getPerson(id);
    }

    @PostMapping
    public PersonDTO createPerson(@RequestBody PersonDTO person) {
        return this.personService.createPerson(person);
    }

    @PutMapping("/{id}")
    public PersonDTO updatePerson(@RequestBody UpdatePersonDTO person, @PathVariable long id) {
        return this.personService.updatePerson(person, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable long id) {
        this.personService.deletePerson(id);
    }
}
