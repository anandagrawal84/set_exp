package com.app.settleexpenses.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Event {
    @SuppressWarnings("unused")
	private int id;

    private String title;

    private List<Expense> expenses;

    public Event(int id, String title, List<Expense> expenses) {
        this.id = id;
        this.title = title;
        this.expenses = expenses;
    }

    public String getTitle() {
        return title;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<Participant> getParticipants() {
        Set<Participant> participants = new HashSet<Participant>();
        for (Expense expense : expenses) {
            participants.add(expense.getPaidBy());
            participants.addAll(expense.getParticipants());
        }
        return new ArrayList<Participant>(participants);
    }

    public List<Settlement> calculateSettlements() {
        ArrayList<Settlement> settlements = new ArrayList<Settlement>();
        ArrayList<ParticipantContribution> participantContributions = calculateParticipantContribution();
        for (ParticipantContribution participantContribution : participantContributions) {
            if (participantContribution.isPayer()) {
                settlements.addAll(payToOneOf(participantContributions, participantContribution));
            } 
        }
        return settlements;
    }

    private ArrayList<Settlement> payToOneOf(ArrayList<ParticipantContribution> participantContributions, ParticipantContribution payer) {
        ArrayList<Settlement> settlements = new ArrayList<Settlement>();
        for (ParticipantContribution participantContribution : participantContributions) {
            if (!participantContribution.isPayer() && !participantContribution.isSettled()) {
                double absoluteSettlementAmount = Math.abs(payer.getContribution());

                if (!participantContribution.canAcceptFullAmount(payer.getContribution())) {
                    absoluteSettlementAmount = Math.abs(payer.getContribution()) - Math.abs(participantContribution.getContribution());
                }

                double settlementAmount = absoluteSettlementAmount * -1;
                participantContribution.addContribution(settlementAmount);
                payer.addContribution(absoluteSettlementAmount);
                settlements.add(new Settlement(payer.getParticipant(), participantContribution.getParticipant(), absoluteSettlementAmount));
                if (payer.getContribution() == 0) return settlements;
            }
        }
        return settlements;
    }

    private ArrayList<ParticipantContribution> calculateParticipantContribution() {
        HashMap<Participant, ParticipantContribution> participantContributions = new HashMap<Participant, ParticipantContribution>();
        for (Expense expense : expenses) {
            for (Participant participant : expense.participantsWithPayer()) {
                ParticipantContribution participantContribution = participantContributions.get(participant);
                if (participantContribution == null) {
                    participantContributions.put(participant, new ParticipantContribution(participant, 0));
                }

                participantContributions.get(participant).addContribution(expense.contributionAmount(participant));
            }
        }
        return sortedContributions(new ArrayList<ParticipantContribution>(participantContributions.values()));
    }

	private ArrayList<ParticipantContribution> sortedContributions(ArrayList<ParticipantContribution> arrayList) {
		Collections.sort(arrayList, new Comparator<ParticipantContribution>() {
			public int compare(ParticipantContribution object1,
					ParticipantContribution object2) {
				return new Double(Math.abs(object2.getContribution())).compareTo(Math.abs(object1.getContribution()));
			}
		});
		return arrayList;
	}
}
