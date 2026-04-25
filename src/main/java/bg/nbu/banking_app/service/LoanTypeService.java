package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Loans.LoanTypes.LoanTypeDTO;
import bg.nbu.banking_app.data.entity.LoanType;

public interface LoanTypeService {
    void createLoanType(LoanTypeDTO loanTypeDTO);
    void deleteLoanType(long loanTypeId);
    LoanTypeDTO getLoanTypeData(long loanTypeId);
}
