package bg.nbu.banking_app.controller;

import bg.nbu.banking_app.data.dto.CompanyDTO;
import bg.nbu.banking_app.data.dto.UpdateCompanyDTO;
import bg.nbu.banking_app.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public List<CompanyDTO> getCompanies() {
        return companyService.getCompanies();
    }

    @GetMapping("/{id}")
    public CompanyDTO getCompany(@PathVariable long id) {
        return this.companyService.getCompany(id);
    }

    @PostMapping
    public CompanyDTO createCompany(@RequestBody CompanyDTO company) {
        return this.companyService.createCompany(company);
    }

    @PutMapping("/{id}")
    public CompanyDTO updateCompany(@RequestBody UpdateCompanyDTO company, @PathVariable long id) {
        return this.companyService.updateCompany(company, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable long id) {
        this.companyService.deleteCompany(id);
    }
}

