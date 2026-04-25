package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.LoanTypes.LoanTypeDTO;
import bg.nbu.banking_app.data.entity.LoanType;
import bg.nbu.banking_app.data.repository.LoanTypeRepository;
import bg.nbu.banking_app.service.LoanTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanTypeServiceImpl implements LoanTypeService {
    private final LoanTypeRepository loanTypeRepository;

    @Override
    public void createLoanType(LoanTypeDTO loanTypeDTO) {
        LoanType loanType = new LoanType();

        loanType.setCreditName(loanTypeDTO.getCreditName());
        loanType.setCreditDisbursedMin(loanTypeDTO.getCreditDisbursedMin());
        loanType.setCreditDisbursedMax(loanTypeDTO.getCreditDisbursedMax());
        loanType.setCreditTermMin(loanTypeDTO.getCreditTermMin());
        loanType.setCreditTermMax(loanTypeDTO.getCreditTermMax());
        loanType.setCreditInterestRate(loanTypeDTO.getCreditInterestRate());

        loanTypeRepository.saveAndFlush(loanType);
    }

    @Override
    public void deleteLoanType(long loanTypeId) {
        loanTypeRepository.deleteById(loanTypeId);
    }

    @Override
    public LoanTypeDTO getLoanTypeData(long loanTypeId) {
        LoanType loanType = loanTypeRepository.findById(loanTypeId).orElse(null);

        if (loanType != null) {
            return new LoanTypeDTO(loanType.getCreditName(),
                    loanType.getCreditDisbursedMin(),
                    loanType.getCreditDisbursedMax(),
                    loanType.getCreditTermMin(),
                    loanType.getCreditTermMax(),
                    loanType.getCreditInterestRate()
            );
        }

        throw new NullPointerException("Loan type not found");
    }
}
