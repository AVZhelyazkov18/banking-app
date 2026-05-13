package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Loans.PaymentPlans.PaymentPlanDTO;
import bg.nbu.banking_app.data.entity.PaymentPlan;
import bg.nbu.banking_app.data.repository.PaymentPlanRepository;
import bg.nbu.banking_app.service.PaymentPlanService;
import bg.nbu.banking_app.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentPlanServiceImpl implements PaymentPlanService {
    private final PaymentPlanRepository paymentPlanRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<PaymentPlanDTO> getPaymentPlans() {
        return this.mapperUtil.mapList(
                this.paymentPlanRepository.findAll(), PaymentPlanDTO.class);
    }

    @Override
    public PaymentPlanDTO getPaymentPlan(long id) {
        PaymentPlan paymentPlan = this.paymentPlanRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentPlan with id " + id + " not found"));

        return this.mapperUtil
                .getModelMapper()
                .map(paymentPlan, PaymentPlanDTO.class);
    }

    @Override
    public PaymentPlanDTO createPaymentPlan(PaymentPlanDTO paymentPlan) {
        return mapperUtil.getModelMapper()
                .map(this.paymentPlanRepository
                        .save(mapperUtil.getModelMapper()
                                .map(paymentPlan, PaymentPlan.class)), PaymentPlanDTO.class);
    }

    @Override
    public PaymentPlanDTO updatePaymentPlan(PaymentPlanDTO paymentPlan, long id) {
        PaymentPlan paymentPlan1 = this.paymentPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentPlan with id " + id + " not found"));

        paymentPlan1.setDate(paymentPlan.getDate());
        paymentPlan1.setInterestPortion(paymentPlan.getInterestPortion());
        paymentPlan1.setPrincipalPortion(paymentPlan.getPrincipalPortion());
        paymentPlan1.setContributionAmount(paymentPlan.getContributionAmount());

        PaymentPlan updated = this.paymentPlanRepository.save(paymentPlan1);

        return this.mapperUtil
                .getModelMapper()
                .map(updated, PaymentPlanDTO.class);
    }

    @Override
    public void deletePaymentPlan(long id) {
        this.paymentPlanRepository.deleteById(id);
    }
}
