package com.sherlockcodes.lendico.service;

import com.sherlockcodes.lendico.PlanGeneratorException;
import com.sherlockcodes.lendico.controllers.PlanController;
import com.sherlockcodes.lendico.models.PaymentInfo;
import com.sherlockcodes.lendico.models.PaymentInput;
import com.sherlockcodes.lendico.models.PaymentPlan;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Service
public class PlanGeneratorService {
    private static final Logger logger = LogManager.getLogger(PlanController.class);
    DecimalFormat df;

    public PlanGeneratorService() {
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);
    }


    public PaymentPlan generatePlan(PaymentInput input) {

        double loanAmount = input.getLoanAmount();
        double monthlyRate = input.getMonthlyRate();
        long duration = input.getDuration();
        if (loanAmount <= 0 || monthlyRate <= 0 || duration <= 0) throw new PlanGeneratorException("Input values cannot be negetive");
        double annuity = getAnnuity(loanAmount, monthlyRate, duration);
        logger.info("for %f ,%f %f annuity has been calculated as %f", loanAmount, monthlyRate, duration, annuity);
        LocalDate startDate = input.getStartDate().toLocalDate();

        PaymentPlan plan = new PaymentPlan();
        for (int i = 0; i < duration; i++) {

            double interest = sanitize(monthlyRate * loanAmount);
            double paymentCapable = sanitize((annuity - interest > loanAmount) ? loanAmount : annuity - interest);

            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setInterest(interest);
            paymentInfo.setPrincipal(paymentCapable);
            paymentInfo.setBorrowerPaymentAmount(sanitize(paymentCapable + interest));
            paymentInfo.setDate(startDate.plusMonths(i));
            paymentInfo.setInitialOutstandingPrincipal(loanAmount);
            paymentInfo.setRemainingOutstandingPrincipal(sanitize(loanAmount - paymentCapable));
            plan.addPlans(paymentInfo);
            loanAmount = sanitize(loanAmount - paymentCapable);
        }
        return plan;
    }

    private double getAnnuity(double loanAmount, double monthlyRate, long duration) {
        return (monthlyRate * loanAmount) / (1 - Math.pow((1 + monthlyRate), -1 * duration));
    }

    private double sanitize(double interest) {
        return Double.parseDouble(df.format(interest));
    }

}
