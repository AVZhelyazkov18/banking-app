package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanDTO;
import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.repository.LoanRepository;
import bg.nbu.banking_app.service.LoanService;
import bg.nbu.banking_app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<LoanDTO> getLoans() {
        return this.mapperUtil.mapList(
                this.loanRepository.findAll(), LoanDTO.class);
    }

    @Override
    public LoanDTO getLoan(long id) {
        Loan loan = this.loanRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Loan with id " + id + " not found"));

        return this.mapperUtil
                .getModelMapper()
                .map(loan, LoanDTO.class);
    }

    @Override
    public LoanDTO createLoan(LoanDTO loan) {
        return mapperUtil.getModelMapper()
                .map(this.loanRepository
                        .save(mapperUtil.getModelMapper()
                                .map(loan, Loan.class)), LoanDTO.class);
    }

    @Override
    public LoanDTO updateLoan(LoanDTO loan, long id) {
        Loan loan1 = this.loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan with id " + id + " not found"));

        loan1.setAmountDisbursed(loan.getAmountDisbursed());
        loan1.setPaymentTerm(loan.getPaymentTerm());
        loan1.setLoanType(loan.getLoanType());

        Loan updated = this.loanRepository.save(loan1);

        return this.mapperUtil
                .getModelMapper()
                .map(updated, LoanDTO.class);
    }

    @Override
    public void deleteLoan(long id) {
        this.loanRepository.deleteById(id);
    }
}
