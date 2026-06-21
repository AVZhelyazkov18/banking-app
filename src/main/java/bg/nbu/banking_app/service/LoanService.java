package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanDTO;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getLoans();
    LoanDTO getLoan(long id);
    LoanDTO createLoan(LoanDTO loan);
    LoanDTO updateLoan(LoanDTO loan, long id);
    void deleteLoan(long id);
    List<LoanDTO> getMyLoans(String username);
    LoanDTO payNextInstallment(long loanId, long bankAccountId, String username);
}
