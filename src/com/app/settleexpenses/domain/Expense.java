package com.app.settleexpenses.domain;

import com.app.settleexpenses.DbAdapter;

import android.content.ContentValues;

public class Expense {

	private String title;
	private float amount;
	private long eventId;
	
	@SuppressWarnings("unused")
	private Participant paidBy;
	
	@SuppressWarnings("unused")
	private Participant[] participant;
	
	public Expense(String title, float amount, long eventId, Participant paidBy, Participant[] participant) {
		this.title = title;
		this.amount = amount;
		this.eventId = eventId;
		this.paidBy = paidBy;
		this.participant = participant;
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
        values.put(DbAdapter.EXPENSE_TITLE, title);
        values.put(DbAdapter.EXPENSE_EVENT_ID, eventId);
        values.put(DbAdapter.EXPENSE_BY, "");
        values.put(DbAdapter.EXPENSE_AMOUNT, amount);
        return values;
	}
	
	
	
}
