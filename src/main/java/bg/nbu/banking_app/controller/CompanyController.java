package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.Customers.Company.CompanyDTO;
import bg.nbu.banking_app.data.dto.Customers.Company.UpdateCompanyDTO;
import bg.nbu.banking_app.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public List<CompanyDTO> getCompanies() {
        return companyService.getCompanies();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public CompanyDTO getCompany(@PathVariable long id) {
        return this.companyService.getCompany(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public CompanyDTO createCompany(@Valid  @RequestBody CompanyDTO company) {
        return this.companyService.createCompany(company); //TODO: Check passing ID inside the RequestBody. Maybe the Backend will need to add it and not to be sned from FE
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public CompanyDTO updateCompany(@Valid @RequestBody UpdateCompanyDTO company, @PathVariable long id) {
        return this.companyService.updateCompany(company, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCompany(@PathVariable long id) {
        this.companyService.deleteCompany(id);
    }
}

