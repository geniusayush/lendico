package com.sherlockcodes.lendico.service;

import com.sherlockcodes.lendico.models.PaymentInput;
import com.sherlockcodes.lendico.models.PaymentPlan;

import java.time.LocalDateTime;

/**An interface representing an general  plan generator with a method generatePlan .
 * This class can be extended by multiple generators to generate required plans
* */
public interface PlanGenerator {
    PaymentPlan generatePlan(PaymentInput input);
}

