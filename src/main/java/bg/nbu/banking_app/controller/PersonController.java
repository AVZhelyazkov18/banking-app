package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Customers.NaturalPerson.PersonDTO;
import bg.nbu.banking_app.data.dto.Customers.NaturalPerson.UpdatePersonDTO;
import bg.nbu.banking_app.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/people")
public class PersonController {
    private final PersonService personService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public List<PersonDTO> getPeople() {
        return personService.getPeople();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PersonDTO getPerson(@PathVariable long id) {
        return this.personService.getPerson(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PersonDTO createPerson(@RequestBody PersonDTO person) {
        return this.personService.createPerson(person);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PersonDTO updatePerson(@RequestBody UpdatePersonDTO person, @PathVariable long id) {
        return this.personService.updatePerson(person, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePerson(@PathVariable long id) {
        this.personService.deletePerson(id);
    }
}
