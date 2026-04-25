package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanDTO;
import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.repository.LoanRepository;
import bg.nbu.banking_app.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;

    @Override
    public Set<LoanDTO> getUserLoansInfo(long userId) {
        return null;
    }

    @Override
    public LoanDTO getUserLoanInfoFromId(long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

        return new LoanDTO(
                loan.getAmountDisbursed(),
                loan.getPaymentTerm(),
                loan.getLoanType(),
                loan.getCustomer()
        );
    }


    @Override
    public void createUserLoan(LoanDTO loanDTO) {
        Loan loan = new Loan();

        loan.setAmountDisbursed(loanDTO.getAmountDisbursed());
        loan.setPaymentTerm(loanDTO.getPaymentTerm());
        loan.setLoanType(loanDTO.getLoanType());
        loan.setCustomer(loanDTO.getCustomer());
        loan.setPaymentPlans(null);

        loanRepository.save(loan);
    }

    @Override
    public void deleteUserLoan(long loanId) {
        loanRepository.deleteById(loanId);
    }
}
