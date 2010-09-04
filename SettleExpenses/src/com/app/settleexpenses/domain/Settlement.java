package com.app.settleexpenses.domain;

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
        return amount;
    }
}
