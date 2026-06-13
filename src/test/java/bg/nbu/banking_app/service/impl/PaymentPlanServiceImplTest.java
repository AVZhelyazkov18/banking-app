package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import bg.nbu.banking_app.data.repository.PaymentPlanRepository;
import bg.nbu.banking_app.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentPlanServiceImplTest {

    @Mock
    private PaymentPlanRepository paymentPlanRepository;

    private final MapperUtil mapperUtil = new MapperUtil();



    @Test
    void generateAnnuityPaymentPlanCreatesEqualMonthlyInstallmentsWithDecreasingInterest() {
        PaymentPlanServiceImpl service = new PaymentPlanServiceImpl(paymentPlanRepository, mapperUtil);

        BigDecimal amountDisbursed = new BigDecimal("10000.00");
        double annualInterestRatePercent = 7.0;
        int paymentTermMonths = 240;
        LocalDate firstPaymentDate = LocalDate.of(2026, 6, 1);

        List<PaymentPlanDTO> result = service.generateAnnuityPaymentPlan(
                amountDisbursed,
                annualInterestRatePercent,
                paymentTermMonths,
                firstPaymentDate
        );

        assertThat(result).hasSize(paymentTermMonths);

        PaymentPlanDTO firstInstallment = result.get(0);
        PaymentPlanDTO secondInstallment = result.get(1);
        PaymentPlanDTO lastInstallment = result.get(result.size() - 1);

        assertThat(firstInstallment.getDate()).isEqualTo(firstPaymentDate);
        assertThat(lastInstallment.getDate()).isEqualTo(firstPaymentDate.plusMonths(paymentTermMonths - 1L));

        BigDecimal firstContributionAmount = firstInstallment.getContributionAmount();
        assertThat(result)
                .extracting(PaymentPlanDTO::getContributionAmount)
                .allSatisfy(contributionAmount ->
                        assertThat(contributionAmount).isEqualByComparingTo(firstContributionAmount));

        assertThat(firstInstallment.getInterestPortion())
                .isGreaterThan(firstInstallment.getPrincipalPortion());
        assertThat(lastInstallment.getPrincipalPortion())
                .isGreaterThan(lastInstallment.getInterestPortion());

        assertThat(secondInstallment.getInterestPortion())
                .isLessThan(firstInstallment.getInterestPortion());
        assertThat(lastInstallment.getPrincipalPortion())
                .isGreaterThan(firstInstallment.getPrincipalPortion());

        result.forEach(installment ->
                assertThat(installment.getPrincipalPortion().add(installment.getInterestPortion()))
                        .isEqualByComparingTo(installment.getContributionAmount()));

        BigDecimal totalPrincipal = result.stream()
                .map(PaymentPlanDTO::getPrincipalPortion)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(totalPrincipal).isCloseTo(amountDisbursed, within(new BigDecimal("0.01")));
    }

    @Test
    void updatePaymentPlanUpdatesAllEditablePaymentFields() {
        PaymentPlan existing = new PaymentPlan();
        existing.setContributionAmount(new BigDecimal("100.00"));
        existing.setPrincipalPortion(new BigDecimal("70.00"));
        existing.setInterestPortion(new BigDecimal("30.00"));
        existing.setDate(LocalDate.of(2026, 6, 1));

        PaymentPlanDTO update = new PaymentPlanDTO(
                1L,
                new BigDecimal("120.00"),
                new BigDecimal("90.00"),
                new BigDecimal("30.00"),
                LocalDate.of(2026, 7, 1)
        );

        when(paymentPlanRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(paymentPlanRepository.save(existing)).thenReturn(existing);

        PaymentPlanServiceImpl service = new PaymentPlanServiceImpl(paymentPlanRepository, mapperUtil);

        PaymentPlanDTO result = service.updatePaymentPlan(update, 1L);

        assertThat(result.getContributionAmount()).isEqualByComparingTo("120.00");
        assertThat(result.getPrincipalPortion()).isEqualByComparingTo("90.00");
        assertThat(result.getInterestPortion()).isEqualByComparingTo("30.00");
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2026, 7, 1));
        verify(paymentPlanRepository).save(existing);
    }

    @Test
    void getPaymentPlanThrowsWhenPaymentPlanDoesNotExist() {
        when(paymentPlanRepository.findById(99L)).thenReturn(Optional.empty());

        PaymentPlanServiceImpl service = new PaymentPlanServiceImpl(paymentPlanRepository, mapperUtil);

        assertThatThrownBy(() -> service.getPaymentPlan(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("PaymentPlan with id 99 not found");
    }

    @Test
    void markInstallmentAsPaidSetsPaidFieldsAndSavesInstallment() {
        PaymentPlan existing = new PaymentPlan();
        existing.setContributionAmount(new BigDecimal("150.00"));
        existing.setPrincipalPortion(new BigDecimal("100.00"));
        existing.setInterestPortion(new BigDecimal("50.00"));
        existing.setDate(LocalDate.of(2026, 6, 1));
        existing.setPaid(false);

        LocalDate paidDate = LocalDate.of(2026, 6, 2);

        when(paymentPlanRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(paymentPlanRepository.save(existing)).thenReturn(existing);

        PaymentPlanServiceImpl service = new PaymentPlanServiceImpl(paymentPlanRepository, mapperUtil);

        PaymentPlanDTO result = service.markInstallmentAsPaid(1L, paidDate);

        assertThat(result.isPaid()).isTrue();
        assertThat(result.getPaidDate()).isEqualTo(paidDate);
        assertThat(existing.isPaid()).isTrue();
        assertThat(existing.getPaidDate()).isEqualTo(paidDate);
        verify(paymentPlanRepository).save(existing);
    }

    @Test
    void markInstallmentAsPaidThrowsWhenPaymentPlanDoesNotExist() {
        when(paymentPlanRepository.findById(99L)).thenReturn(Optional.empty());

        PaymentPlanServiceImpl service = new PaymentPlanServiceImpl(paymentPlanRepository, mapperUtil);

        assertThatThrownBy(() -> service.markInstallmentAsPaid(99L, LocalDate.of(2026, 6, 2)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("PaymentPlan with id 99 not found");
    }

    @Test
    void markInstallmentAsPaidThrowsWhenPaymentPlanIsAlreadyPaid() {
        PaymentPlan existing = new PaymentPlan();
        existing.setPaid(true);
        existing.setPaidDate(LocalDate.of(2026, 6, 2));

        when(paymentPlanRepository.findById(1L)).thenReturn(Optional.of(existing));

        PaymentPlanServiceImpl service = new PaymentPlanServiceImpl(paymentPlanRepository, mapperUtil);

        assertThatThrownBy(() -> service.markInstallmentAsPaid(1L, LocalDate.of(2026, 6, 3)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PaymentPlan with id 1 is already paid");

        assertThat(existing.getPaidDate()).isEqualTo(LocalDate.of(2026, 6, 2));

    }
}
