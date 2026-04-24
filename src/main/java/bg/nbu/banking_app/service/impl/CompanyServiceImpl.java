package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.CompanyDTO;
import bg.nbu.banking_app.data.dto.UpdateCompanyDTO;
import bg.nbu.banking_app.data.entity.Company;
import bg.nbu.banking_app.data.repository.CompanyRepository;
import bg.nbu.banking_app.service.CompanyService;
import bg.nbu.banking_app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    CompanyRepository companyRepository;
    MapperUtil mapperUtil;

    @Override
    public List<CompanyDTO> getCompanies() {
        return this.mapperUtil.mapList(
                this.companyRepository.findAll(), CompanyDTO.class);
    }

    @Override
    public CompanyDTO getCompany(long id) {
        Company company = this.companyRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Company with id " + id + " not found"));

        return this.mapperUtil
                .getModelMapper()
                .map(company, CompanyDTO.class);
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO company) {
        return mapperUtil.getModelMapper()
                .map(this.companyRepository
                        .save(mapperUtil.getModelMapper()
                                .map(company, Company.class)), CompanyDTO.class);
    }

    @Override
    public CompanyDTO updateCompany(UpdateCompanyDTO company, long id) {
        Company company1 = this.companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company with id " + id + " not found"));

        company1.setCompanyName(company.getCompanyName());
        company1.setRepresentative(company.getRepresentative());

        Company updated = this.companyRepository.save(company1);

        return this.mapperUtil
                .getModelMapper()
                .map(updated, CompanyDTO.class);
    }

    @Override
    public void deleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
