package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.ClientLoans.LoanDTO;
import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.repository.LoanRepository;
import bg.nbu.banking_app.data.repository.PaymentPlanRepository;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.service.LoanService;
import bg.nbu.banking_app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import bg.nbu.banking_app.data.entity.Customer;
import bg.nbu.banking_app.data.entity.LoanType;
import bg.nbu.banking_app.data.repository.CustomerRepository;
import bg.nbu.banking_app.data.repository.LoanTypeRepository;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final LoanTypeRepository loanTypeRepository;
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
    public LoanDTO createLoan(LoanDTO loanDTO) {
        Customer customer = customerRepository.findById(loanDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer with id " + loanDTO.getCustomerId() + " not found"));

        Long loanTypeId = loanDTO.getLoanType() != null ? loanDTO.getLoanType().getId() : null;

        if (loanTypeId == null) {
            throw new RuntimeException("Loan type is required");
        }

        LoanType loanType = loanTypeRepository.findById(loanTypeId)
                .orElseThrow(() -> new RuntimeException("LoanType with id " + loanTypeId + " not found"));

        Loan loan = new Loan();
        loan.setAmountDisbursed(loanDTO.getAmountDisbursed());
        loan.setPaymentTerm(loanDTO.getPaymentTerm());
        loan.setCustomer(customer);
        loan.setLoanType(loanType);

        Loan saved = loanRepository.save(loan);

        LoanDTO result = mapperUtil.getModelMapper().map(saved, LoanDTO.class);
        result.setCustomerId(customer.getId());

        return result;
    }

    @Override
    public LoanDTO updateLoan(LoanDTO loanDTO, long id) {
        Loan loan = this.loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan with id " + id + " not found"));

        loan.setAmountDisbursed(loanDTO.getAmountDisbursed());
        loan.setPaymentTerm(loanDTO.getPaymentTerm());
        //|| loanDTO.getLoanType().getId() == null \/\/\/\/\/
        if (loanDTO.getLoanType() == null ) {
            throw new RuntimeException("Loan type is required");
        }

        LoanType loanType = loanTypeRepository.findById(loanDTO.getLoanType().getId())
                .orElseThrow(() -> new RuntimeException("LoanType with id " + loanDTO.getLoanType().getId() + " not found"));

        loan.setLoanType(loanType);

        if (loanDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(loanDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer with id " + loanDTO.getCustomerId() + " not found"));

            loan.setCustomer(customer);
        }

        Loan updated = this.loanRepository.save(loan);

        LoanDTO result = this.mapperUtil.getModelMapper().map(updated, LoanDTO.class);
        if (updated.getCustomer() != null) {
            result.setCustomerId(updated.getCustomer().getId());
        }

        return result;
    }

    @Override
    public void deleteLoan(long id) {
        this.loanRepository.deleteById(id);
    }

    @Override
    public List<LoanDTO> getMyLoans(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        if (user.getCustomer() == null) return List.of();
        List<Loan> loans = loanRepository.findByCustomerId(user.getCustomer().getId());
        return loans.stream().map(loan -> {
            LoanDTO dto = this.mapperUtil.getModelMapper().map(loan, LoanDTO.class);
            paymentPlanRepository.findByLoanId(loan.getId()).stream()
                    .filter(pp -> pp.getDate() != null && !pp.getDate().isBefore(LocalDate.now()))
                    .min(Comparator.comparing(PaymentPlan::getDate))
                    .ifPresent(pp -> {
                        dto.setCurrentPayment(pp.getContributionAmount());
                        dto.setNextPaymentDate(pp.getDate());
                    });
            return dto;
        }).collect(Collectors.toList());
    }
}
