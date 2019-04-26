package com.sherlockcodes.lendico.models;

public class PaymentDTO {
    String loanAmount;
    String nominalRate;
    String duration;
    String startDat;

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getNominalRate() {
        return nominalRate;
    }

    public void setNominalRate(String nominalRate) {
        this.nominalRate = nominalRate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartDat() {
        return startDat;
    }

    public void setStartDat(String startDat) {
        this.startDat = startDat;
    }
}

