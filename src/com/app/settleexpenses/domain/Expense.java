package com.app.settleexpenses.domain;

import com.app.settleexpenses.DbAdapter;

import android.content.ContentValues;

import java.util.ArrayList;

public class Expense {

	private String title;
	private float amount;
	private long eventId;
	
	@SuppressWarnings("unused")
	private Participant paidBy;
	
	@SuppressWarnings("unused")
	private ArrayList<Participant> participants;
	
	public Expense(String title, float amount, long eventId, Participant paidBy, ArrayList<Participant> participants) {
		this.title = title;
		this.amount = amount;
		this.eventId = eventId;
		this.paidBy = paidBy;
		this.participants = participants;
	}

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
        values.put(DbAdapter.EXPENSE_TITLE, title);
        values.put(DbAdapter.EXPENSE_EVENT_ID, eventId);
        values.put(DbAdapter.EXPENSE_BY, paidBy.getId());
        values.put(DbAdapter.EXPENSE_AMOUNT, amount);
        return values;
	}
	
}
