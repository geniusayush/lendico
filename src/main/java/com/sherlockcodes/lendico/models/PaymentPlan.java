package com.sherlockcodes.lendico.models;

import java.util.ArrayList;
import java.util.List;

public class PaymentPlan {
    List<PaymentInfo> plans;

    public PaymentPlan() {
        this.plans = new ArrayList<>();
    }

    public List<PaymentInfo> getPlans() {
        return plans;
    }

    public void addPlans(PaymentInfo plan) {
        this.plans.add(plan);
    }

}
