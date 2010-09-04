package com.app.settleexpenses.domain;

import android.content.ContentValues;
import com.app.settleexpenses.DbAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Expense {

	private String title;
	private float amount;
	private long eventId;

	private Participant paidBy;
	private ArrayList<Participant> participants;

	public Expense(String title, float amount, long eventId,
			Participant paidBy, ArrayList<Participant> participants) {
		this.title = title;
		this.amount = amount;
		this.eventId = eventId;
		this.paidBy = paidBy;
		this.participants = participants;
	}

	public ArrayList<Participant> getParticipants() {
		return participants;
	}


	public ArrayList<Participant> participantsWithPayer() {
		if(isPayerPartOfExpense()) {
			return participants;
		}
		ArrayList<Participant> participantWithPayer = new ArrayList<Participant>();
		participantWithPayer.addAll(participants);
		participantWithPayer.add(paidBy);
		return participantWithPayer;
	}
	
	public Participant getPaidBy() {
		return paidBy;
	}

	public float getAmount() {
		return amount;
	}

	public String getTitle() {
		return title;
	}

	public float contributionAmount(Participant participant) {
		float contributionAmount = amount / participants.size();
		if (paidBy.getId().equals(participant.getId())) {
			if (isPayerPartOfExpense()) {
				return amount - contributionAmount;
			} else {
				return amount;
			}
		}
		return contributionAmount * -1;
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(DbAdapter.EXPENSE_TITLE, title);
		values.put(DbAdapter.EXPENSE_EVENT_ID, eventId);
		values.put(DbAdapter.EXPENSE_BY, paidBy.getId());
		values.put(DbAdapter.EXPENSE_AMOUNT, amount);
		return values;
	}

	private boolean isPayerPartOfExpense() {
		for (Participant participant : participants) {
			if (paidBy.getId().equals(participant.getId())) {
				return true;
			}
		}
		return false;
	}

}
