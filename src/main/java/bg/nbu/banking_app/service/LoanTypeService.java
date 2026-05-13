package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Loans.LoanTypes.LoanTypeDTO;

import java.util.List;

public interface LoanTypeService {
    List<LoanTypeDTO> getLoanTypes();
    LoanTypeDTO getLoanType(long id);
    LoanTypeDTO createLoanType(LoanTypeDTO loanType);
    LoanTypeDTO updateLoanType(LoanTypeDTO loanType, long id);
    void deleteLoanType(long id);
}
