package com.app.settleexpenses.domain;

import java.text.DecimalFormat;

public class ParticipantContribution {
    private Participant participant;
    private float contribution;

    public ParticipantContribution(Participant participant, float contribution) {
        this.participant = participant;
        this.contribution = contribution;
    }

    public void addContribution(double amount) {
        contribution+=amount;
    }

    public boolean isPayer() {
        return contribution < 0;
    }

    public boolean isSettled() {
        return ((int)contribution == 0);
    }

    public Double getContribution() {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(contribution));
    }

    public boolean canAcceptFullAmount(double contribution) {
        return !isPayer() && (Math.abs(this.contribution) >= Math.abs(contribution));
    }

    public Participant getParticipant() {
        return participant;
    }
}
