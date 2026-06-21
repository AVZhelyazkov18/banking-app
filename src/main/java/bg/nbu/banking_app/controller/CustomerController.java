package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Customers.CustomerOptionDTO;
import bg.nbu.banking_app.data.entity.Company;
import bg.nbu.banking_app.data.entity.Customer;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping("/options")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public List<CustomerOptionDTO> getCustomerOptions() {
        return customerRepository.findAll()
                .stream()
                .map(this::toOption)
                .sorted(Comparator.comparing(CustomerOptionDTO::getDisplayName))
                .toList();
    }

    private CustomerOptionDTO toOption(Customer customer) {
        if (customer instanceof Person person) {
            String displayName = person.getFirstName() + " " + person.getLastName();

            return new CustomerOptionDTO(
                    person.getId(),
                    displayName,
                    "PERSON"
            );
        }

        if (customer instanceof Company company) {
            return new CustomerOptionDTO(
                    company.getId(),
                    company.getCompanyName(),
                    "COMPANY"
            );
        }

        return new CustomerOptionDTO(
                customer.getId(),
                "Customer #" + customer.getId(),
                "CUSTOMER"
        );
    }
}