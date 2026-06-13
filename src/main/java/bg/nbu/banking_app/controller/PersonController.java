package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Customers.NaturalPerson.PersonDTO;
import bg.nbu.banking_app.data.dto.Customers.NaturalPerson.UpdatePersonDTO;
import bg.nbu.banking_app.service.PersonService;
import jakarta.validation.Valid;
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
    public PersonDTO createPerson(@Valid @RequestBody PersonDTO person) {
        return this.personService.createPerson(person); //TODO: Check passing ID inside the RequestBody. Maybe the Backend will need to add it and not to be sned from FE
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public PersonDTO updatePerson(@Valid @RequestBody UpdatePersonDTO person, @PathVariable long id) {
        return this.personService.updatePerson(person, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePerson(@PathVariable long id) {
        this.personService.deletePerson(id); //TODO: Check FK for Customer when deleting it. It needs to be removed as well. Add check if ID exists before deleting it.
    }
}
