package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.LoanTypes.LoanTypeDTO;
import bg.nbu.banking_app.data.entity.LoanType;
import bg.nbu.banking_app.data.repository.LoanTypeRepository;
import bg.nbu.banking_app.service.LoanTypeService;
import bg.nbu.banking_app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanTypeServiceImpl implements LoanTypeService {
    private final LoanTypeRepository loanTypeRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<LoanTypeDTO> getLoanTypes() {
        return this.mapperUtil.mapList(
                this.loanTypeRepository.findAll(), LoanTypeDTO.class);
    }

    @Override
    public LoanTypeDTO getLoanType(long id) {
        LoanType loanType = this.loanTypeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("LoanType with id " + id + " not found"));

        return this.mapperUtil
                .getModelMapper()
                .map(loanType, LoanTypeDTO.class);
    }

    @Override
    public LoanTypeDTO createLoanType(LoanTypeDTO loanType) {
        return mapperUtil.getModelMapper()
                .map(this.loanTypeRepository
                        .save(mapperUtil.getModelMapper()
                                .map(loanType, LoanType.class)), LoanTypeDTO.class);
    }

    @Override
    public LoanTypeDTO updateLoanType(LoanTypeDTO loanType, long id) {
        LoanType loanType1 = this.loanTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LoanType with id " + id + " not found"));

        loanType1.setCreditInterestRate(loanType.getCreditInterestRate());
        loanType1.setCreditDisbursedMax(loanType.getCreditDisbursedMax());
        loanType1.setCreditDisbursedMin(loanType.getCreditDisbursedMin());
        loanType1.setCreditName(loanType.getCreditName());
        loanType1.setCreditTermMin(loanType.getCreditTermMin());
        loanType1.setCreditTermMax(loanType.getCreditTermMax());

        LoanType updated = this.loanTypeRepository.save(loanType1);

        return this.mapperUtil
                .getModelMapper()
                .map(updated, LoanTypeDTO.class);
    }

    @Override
    public void deleteLoanType(long id) {
        this.loanTypeRepository.deleteById(id);
    }
}
