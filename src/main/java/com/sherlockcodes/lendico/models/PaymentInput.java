package com.sherlockcodes.lendico.models;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class PaymentInput {
    private double loanAmount;
    private double monthlyRate;
    private long duration;
    private LocalDateTime startDate;

    public PaymentInput(PaymentDTO dto) {

        duration = Long.parseLong(dto.getDuration());
        loanAmount = Double.parseDouble(dto.getLoanAmount());
        startDate = ZonedDateTime.parse(dto.getStartDat()).toLocalDateTime();
        monthlyRate = Double.parseDouble(dto.getNominalRate()) / 12 / 100.00;
        if (loanAmount <= 0 || monthlyRate <= 0 || duration <= 0) throw new IllegalArgumentException();
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public double getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(double monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
