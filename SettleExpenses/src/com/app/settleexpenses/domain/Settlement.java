package com.app.settleexpenses.domain;

import java.text.DecimalFormat;

public class Settlement {
    private Participant paidBy;
    private Participant toPay;
    private double amount;

    public Settlement(Participant paidBy, Participant toPay, double amount) {
        this.paidBy = paidBy;
        this.toPay = toPay;
        this.amount = amount;
    }

    public Participant payer() {
        return paidBy;
    }

    public Participant receiver() {
        return toPay;
    }

    public double getAmount() {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(amount));
    }
}
