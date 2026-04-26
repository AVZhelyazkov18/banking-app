package bg.nbu.banking_app.service;


import bg.nbu.banking_app.data.dto.Customers.Company.CompanyDTO;
import bg.nbu.banking_app.data.dto.Customers.Company.UpdateCompanyDTO;

import java.util.List;

public interface CompanyService {
    List<CompanyDTO> getCompanies();
    CompanyDTO getCompany(long id);
    CompanyDTO createCompany(CompanyDTO company);
    CompanyDTO updateCompany(UpdateCompanyDTO company, long id);
    void deleteCompany(long id);
}
