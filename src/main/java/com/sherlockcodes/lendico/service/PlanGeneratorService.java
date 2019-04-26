package com.sherlockcodes.lendico.service;

import com.sherlockcodes.lendico.models.PaymentInfo;
import com.sherlockcodes.lendico.models.PaymentInput;
import com.sherlockcodes.lendico.models.PaymentPlan;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Service
public class PlanGeneratorService implements PlanGenerator {
    DecimalFormat df;

    public PlanGeneratorService() {
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
    }

    @Override
    public PaymentPlan generatePlan(PaymentInput input) {

        double loanAmount = input.getLoanAmount();
        double monthlyRate = input.getMonthlyRate();
        long duration = input.getDuration();
        double annuity = getAnnuity(loanAmount, monthlyRate, duration);
        LocalDate startDate = input.getStartDate().toLocalDate();

        PaymentPlan plan = new PaymentPlan();
        for (int i = 0; i < duration; i++) {

            double interest = sanitize(monthlyRate * loanAmount);
            double paymentCapable  = sanitize( (annuity - interest > loanAmount) ? loanAmount : annuity - interest);

            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setInterest(interest);
            paymentInfo.setPrincipal(paymentCapable);
            paymentInfo.setBorrowerPaymentAmount(sanitize(paymentCapable + interest));
            paymentInfo.setDate(startDate.plusMonths(i));
            paymentInfo.setInitialOutstandingPrincipal(loanAmount);
            paymentInfo.setRemainingOutstandingPrincipal( sanitize(loanAmount - paymentCapable));
            plan.addPlans(paymentInfo);
            loanAmount = sanitize(loanAmount - paymentCapable);
        }
        return plan;
    }

    public double getAnnuity(double loanAmount, double monthlyRate, long duration) {
        return (monthlyRate * loanAmount) / (1 - Math.pow((1 + monthlyRate), -1 * duration));
    }

    private double sanitize(double interest) {
        return Double.parseDouble(df.format(interest));
    }

}
