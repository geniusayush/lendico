package com.sherlockcodes.lendico.unit;

import com.sherlockcodes.lendico.models.PaymentDTO;
import com.sherlockcodes.lendico.models.PaymentInfo;
import com.sherlockcodes.lendico.models.PaymentInput;
import com.sherlockcodes.lendico.models.PaymentPlan;
import com.sherlockcodes.lendico.service.PlanGeneratorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ServiceTest {

    PlanGeneratorService generatorService = new PlanGeneratorService();
    private PaymentInput paymetnInfo;
    private Integer terms;


    public ServiceTest(PaymentInput paymetnInfo, Integer terms) {
        this.paymetnInfo = paymetnInfo;
        this.terms = terms;
    }

    // Each parameter should be placed as an argument here
    // Every time runner triggers, it will pass the arguments
    // from parameters we defined in primeNumbers() method

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new Object[][]{
                {new PaymentInput(new PaymentDTO("5000", "5", 24,
                        ZonedDateTime.of(LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0), ZoneId.of("Z")).toString())), 24},
                {new PaymentInput(new PaymentDTO("5000", "5", 1,
                        ZonedDateTime.of(LocalDateTime.of(2018, Month.JANUARY, 1, 0, 0, 0), ZoneId.of("Z")).toString())), 1},
        });
    }

    @Test()
    public void sanityTestPaymentsSize() {
        PaymentPlan plan = generatorService.generatePlan(paymetnInfo);
        assertTrue(terms==plan.getPlans().size());
    }

    @Test()
    public void sanityTestLastItemHas0RemainingPaymentsLeft() {
        PaymentPlan plan = generatorService.generatePlan(paymetnInfo);
        assertEquals(0, plan.getPlans().get(plan.getPlans().size() - 1).getRemainingOutstandingPrincipal(), 0.6);

    }

    @Test()
    public void testAllPaymentsAreSameExceptLast() {
        PaymentPlan plan = generatorService.generatePlan(paymetnInfo);
        long uniquePayments = plan.getPlans().stream().map(PaymentInfo::getBorrowerPaymentAmount).distinct().count();
        assertTrue(uniquePayments < 3);

    }

    @Test()
    public void testPaymentCalcualtionsCorrect() {
        PaymentPlan plan = generatorService.generatePlan(paymetnInfo);
        plan.getPlans().forEach(this::validatePlan);

    }


    private void validatePlan(PaymentInfo plan) {
        assertEquals(plan.getBorrowerPaymentAmount(), (plan.getPrincipal() + plan.getInterest()), 0.5);

        if (plan.getRemainingOutstandingPrincipal() != 0) {
            assertEquals(plan.getInitialOutstandingPrincipal(), (plan.getRemainingOutstandingPrincipal() + plan.getPrincipal()), 0.5);
        }
    }


}
