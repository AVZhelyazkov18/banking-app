package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanDTO;
import bg.nbu.banking_app.data.entity.Loan;

import java.util.Set;

public interface LoanService {
    Set<LoanDTO> getUserLoansInfo(long userId);
    LoanDTO getUserLoanInfoFromId(long loanId);
    void createUserLoan(LoanDTO loanDTO);
    void deleteUserLoan(long loanId);
}
