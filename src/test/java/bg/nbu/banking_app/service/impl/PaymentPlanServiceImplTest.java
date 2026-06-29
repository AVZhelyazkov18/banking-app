package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;
import bg.nbu.banking_app.data.entity.Loan;
import bg.nbu.banking_app.data.entity.LoanType;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import bg.nbu.banking_app.data.repository.LoanRepository;
import bg.nbu.banking_app.data.repository.PaymentPlanRepository;
import bg.nbu.banking_app.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentPlanServiceImplTest {

    @Mock
    private PaymentPlanRepository paymentPlanRepository;

    @Mock
    private MapperUtil mapperUtil;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private PaymentPlanServiceImpl paymentPlanService;

    private void mockModelMapper() {
        when(mapperUtil.getModelMapper()).thenReturn(new ModelMapper());
    }

    private void setId(Object entity, long id) {
        ReflectionTestUtils.setField(entity, "id", id);
    }

    @Test
    void getPaymentPlansShouldReturnMappedDtos() {
        PaymentPlan plan1 = new PaymentPlan();
        PaymentPlan plan2 = new PaymentPlan();

        PaymentPlanDTO dto1 = new PaymentPlanDTO();
        PaymentPlanDTO dto2 = new PaymentPlanDTO();

        List<PaymentPlan> plans = List.of(plan1, plan2);
        List<PaymentPlanDTO> dtos = List.of(dto1, dto2);

        when(paymentPlanRepository.findAll()).thenReturn(plans);
        when(mapperUtil.mapList(plans, PaymentPlanDTO.class)).thenReturn(dtos);

        List<PaymentPlanDTO> result = paymentPlanService.getPaymentPlans();

        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));

        verify(paymentPlanRepository).findAll();
        verify(mapperUtil).mapList(plans, PaymentPlanDTO.class);
    }

    @Test
    void getPaymentPlanShouldReturnDtoWhenExists() {
        mockModelMapper();

        PaymentPlan plan = new PaymentPlan();
        setId(plan, 1L);
        plan.setContributionAmount(new BigDecimal("100.00"));
        plan.setPrincipalPortion(new BigDecimal("80.00"));
        plan.setInterestPortion(new BigDecimal("20.00"));
        plan.setDate(LocalDate.of(2026, 7, 1));

        when(paymentPlanRepository.findById(1L)).thenReturn(Optional.of(plan));

        PaymentPlanDTO result = paymentPlanService.getPaymentPlan(1L);

        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.getContributionAmount());
        assertEquals(new BigDecimal("80.00"), result.getPrincipalPortion());
        assertEquals(new BigDecimal("20.00"), result.getInterestPortion());
        assertEquals(LocalDate.of(2026, 7, 1), result.getDate());

        verify(paymentPlanRepository).findById(1L);
    }

    @Test
    void getPaymentPlanShouldThrowWhenMissing() {
        when(paymentPlanRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentPlanService.getPaymentPlan(99L)
        );

        assertEquals("PaymentPlan with id 99 not found", exception.getMessage());

        verify(paymentPlanRepository).findById(99L);
        verify(mapperUtil, never()).getModelMapper();
    }

    @Test
    void createPaymentPlanShouldMapSaveAndReturnDto() {
        mockModelMapper();

        PaymentPlanDTO request = new PaymentPlanDTO(
                null,
                new BigDecimal("120.00"),
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                LocalDate.of(2026, 7, 1)
        );

        ArgumentCaptor<PaymentPlan> captor = ArgumentCaptor.forClass(PaymentPlan.class);

        when(paymentPlanRepository.save(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentPlanDTO result = paymentPlanService.createPaymentPlan(request);

        PaymentPlan savedEntity = captor.getValue();

        assertNotNull(result);
        assertEquals(new BigDecimal("120.00"), savedEntity.getContributionAmount());
        assertEquals(new BigDecimal("100.00"), savedEntity.getPrincipalPortion());
        assertEquals(new BigDecimal("20.00"), savedEntity.getInterestPortion());
        assertEquals(LocalDate.of(2026, 7, 1), savedEntity.getDate());

        verify(paymentPlanRepository).save(any(PaymentPlan.class));
    }

    @Test
    void updatePaymentPlanShouldUpdateFieldsAndSave() {
        mockModelMapper();

        PaymentPlan existing = new PaymentPlan();
        setId(existing, 1L);
        existing.setContributionAmount(new BigDecimal("100.00"));
        existing.setPrincipalPortion(new BigDecimal("80.00"));
        existing.setInterestPortion(new BigDecimal("20.00"));
        existing.setDate(LocalDate.of(2026, 7, 1));

        PaymentPlanDTO request = new PaymentPlanDTO(
                1L,
                new BigDecimal("150.00"),
                new BigDecimal("120.00"),
                new BigDecimal("30.00"),
                LocalDate.of(2026, 8, 1)
        );

        when(paymentPlanRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(paymentPlanRepository.save(existing)).thenReturn(existing);

        PaymentPlanDTO result = paymentPlanService.updatePaymentPlan(request, 1L);

        assertNotNull(result);
        assertEquals(new BigDecimal("150.00"), existing.getContributionAmount());
        assertEquals(new BigDecimal("120.00"), existing.getPrincipalPortion());
        assertEquals(new BigDecimal("30.00"), existing.getInterestPortion());
        assertEquals(LocalDate.of(2026, 8, 1), existing.getDate());

        verify(paymentPlanRepository).save(existing);
    }

    @Test
    void updatePaymentPlanShouldThrowWhenMissing() {
        PaymentPlanDTO request = new PaymentPlanDTO(
                99L,
                new BigDecimal("150.00"),
                new BigDecimal("120.00"),
                new BigDecimal("30.00"),
                LocalDate.of(2026, 8, 1)
        );

        when(paymentPlanRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentPlanService.updatePaymentPlan(request, 99L)
        );

        assertEquals("PaymentPlan with id 99 not found", exception.getMessage());

        verify(paymentPlanRepository).findById(99L);
        verify(paymentPlanRepository, never()).save(any());
    }

    @Test
    void deletePaymentPlanShouldDeleteById() {
        paymentPlanService.deletePaymentPlan(1L);

        verify(paymentPlanRepository).deleteById(1L);
    }

    @Test
    void markInstallmentAsPaidShouldSetPaidFieldsAndSave() {
        mockModelMapper();

        PaymentPlan plan = new PaymentPlan();
        setId(plan, 1L);
        plan.setPaid(false);
        plan.setContributionAmount(new BigDecimal("100.00"));
        plan.setPrincipalPortion(new BigDecimal("80.00"));
        plan.setInterestPortion(new BigDecimal("20.00"));
        plan.setDate(LocalDate.of(2026, 7, 1));

        LocalDate paidDate = LocalDate.of(2026, 7, 5);

        when(paymentPlanRepository.findById(1L)).thenReturn(Optional.of(plan));
        when(paymentPlanRepository.save(plan)).thenReturn(plan);

        PaymentPlanDTO result = paymentPlanService.markInstallmentAsPaid(1L, paidDate);

        assertNotNull(result);
        assertTrue(plan.isPaid());
        assertEquals(paidDate, plan.getPaidDate());

        verify(paymentPlanRepository).findById(1L);
        verify(paymentPlanRepository).save(plan);
    }

    @Test
    void markInstallmentAsPaidShouldThrowWhenPaidDateIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentPlanService.markInstallmentAsPaid(1L, null)
        );

        assertEquals("Payment date is required", exception.getMessage());

        verify(paymentPlanRepository, never()).findById(anyLong());
        verify(paymentPlanRepository, never()).save(any());
    }

    @Test
    void markInstallmentAsPaidShouldThrowWhenPaymentPlanDoesNotExist() {
        when(paymentPlanRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentPlanService.markInstallmentAsPaid(99L, LocalDate.now())
        );

        assertEquals("PaymentPlan with id 99 not found", exception.getMessage());

        verify(paymentPlanRepository).findById(99L);
        verify(paymentPlanRepository, never()).save(any());
    }

    @Test
    void markInstallmentAsPaidShouldThrowWhenAlreadyPaid() {
        PaymentPlan plan = new PaymentPlan();
        setId(plan, 1L);
        plan.setPaid(true);
        plan.setPaidDate(LocalDate.of(2026, 7, 1));

        when(paymentPlanRepository.findById(1L)).thenReturn(Optional.of(plan));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> paymentPlanService.markInstallmentAsPaid(1L, LocalDate.of(2026, 7, 5))
        );

        assertEquals("PaymentPlan with id 1 is already paid", exception.getMessage());

        verify(paymentPlanRepository).findById(1L);
        verify(paymentPlanRepository, never()).save(any());
    }

    @Test
    void getPaymentPlanFromLoanShouldReturnExistingPlansWithoutGeneratingNewOnes() {
        mockModelMapper();

        Loan loan = new Loan();
        setId(loan, 10L);

        PaymentPlan laterPlan = new PaymentPlan();
        setId(laterPlan, 2L);
        laterPlan.setLoan(loan);
        laterPlan.setContributionAmount(new BigDecimal("200.00"));
        laterPlan.setPrincipalPortion(new BigDecimal("180.00"));
        laterPlan.setInterestPortion(new BigDecimal("20.00"));
        laterPlan.setDate(LocalDate.of(2026, 8, 1));

        PaymentPlan earlierPlan = new PaymentPlan();
        setId(earlierPlan, 1L);
        earlierPlan.setLoan(loan);
        earlierPlan.setContributionAmount(new BigDecimal("100.00"));
        earlierPlan.setPrincipalPortion(new BigDecimal("90.00"));
        earlierPlan.setInterestPortion(new BigDecimal("10.00"));
        earlierPlan.setDate(LocalDate.of(2026, 7, 1));

        loan.setPaymentPlans(List.of(laterPlan, earlierPlan));

        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));

        List<PaymentPlanDTO> result = paymentPlanService.getPaymentPlanFromLoan(10L);

        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2026, 7, 1), result.get(0).getDate());
        assertEquals(LocalDate.of(2026, 8, 1), result.get(1).getDate());

        verify(paymentPlanRepository, never()).saveAll(anyList());
    }

    @Test
    void getPaymentPlanFromLoanShouldGenerateAndSaveAnnuityPlanWhenLoanHasNoPlans() {
        mockModelMapper();

        Loan loan = new Loan();
        setId(loan, 10L);
        loan.setAmountDisbursed(new BigDecimal("20000.00"));
        loan.setPaymentTerm(18);
        loan.setPaymentPlans(new ArrayList<>());

        LoanType loanType = new LoanType();
        loanType.setCreditInterestRate(6.25);
        loan.setLoanType(loanType);

        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));

        ArgumentCaptor<List<PaymentPlan>> captor = ArgumentCaptor.forClass(List.class);

        when(paymentPlanRepository.saveAll(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<PaymentPlanDTO> result = paymentPlanService.getPaymentPlanFromLoan(10L);

        List<PaymentPlan> savedPlans = captor.getValue();

        assertEquals(18, result.size());
        assertEquals(18, savedPlans.size());

        PaymentPlan first = savedPlans.stream()
                .min(Comparator.comparing(PaymentPlan::getDate))
                .orElseThrow();

        assertSame(loan, first.getLoan());
        assertEquals(new BigDecimal("1166.90"), first.getContributionAmount());
        assertEquals(new BigDecimal("104.17"), first.getInterestPortion());
        assertEquals(new BigDecimal("1062.73"), first.getPrincipalPortion());
        assertFalse(first.isPaid());
        assertNull(first.getPaidDate());

        BigDecimal totalPrincipal = savedPlans.stream()
                .map(PaymentPlan::getPrincipalPortion)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertEquals(new BigDecimal("20000.00"), totalPrincipal);

        verify(paymentPlanRepository).saveAll(anyList());
    }

    @Test
    void getPaymentPlanFromLoanShouldThrowWhenLoanDoesNotExist() {
        when(loanRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentPlanService.getPaymentPlanFromLoan(99L)
        );

        assertEquals("Loan not found", exception.getMessage());

        verify(loanRepository).findById(99L);
        verify(paymentPlanRepository, never()).saveAll(anyList());
    }

    @Test
    void getPaymentPlanFromLoanShouldThrowWhenLoanAmountIsInvalid() {
        Loan loan = new Loan();
        setId(loan, 10L);
        loan.setAmountDisbursed(BigDecimal.ZERO);
        loan.setPaymentTerm(18);
        loan.setPaymentPlans(new ArrayList<>());

        LoanType loanType = new LoanType();
        loanType.setCreditInterestRate(6.25);
        loan.setLoanType(loanType);

        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentPlanService.getPaymentPlanFromLoan(10L)
        );

        assertEquals("Loan amount must be greater than 0", exception.getMessage());

        verify(paymentPlanRepository, never()).saveAll(anyList());
    }

    @Test
    void getPaymentPlanFromLoanShouldThrowWhenPaymentTermIsInvalid() {
        Loan loan = new Loan();
        setId(loan, 10L);
        loan.setAmountDisbursed(new BigDecimal("20000.00"));
        loan.setPaymentTerm(0);
        loan.setPaymentPlans(new ArrayList<>());

        LoanType loanType = new LoanType();
        loanType.setCreditInterestRate(6.25);
        loan.setLoanType(loanType);

        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentPlanService.getPaymentPlanFromLoan(10L)
        );

        assertEquals("Payment term must be greater than 0", exception.getMessage());

        verify(paymentPlanRepository, never()).saveAll(anyList());
    }

    @Test
    void getPaymentPlanFromLoanShouldThrowWhenInterestIsNegative() {
        Loan loan = new Loan();
        setId(loan, 10L);
        loan.setAmountDisbursed(new BigDecimal("20000.00"));
        loan.setPaymentTerm(18);
        loan.setPaymentPlans(new ArrayList<>());

        LoanType loanType = new LoanType();
        loanType.setCreditInterestRate(-1);
        loan.setLoanType(loanType);

        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentPlanService.getPaymentPlanFromLoan(10L)
        );

        assertEquals("Interest rate cannot be negative", exception.getMessage());

        verify(paymentPlanRepository, never()).saveAll(anyList());
    }
}