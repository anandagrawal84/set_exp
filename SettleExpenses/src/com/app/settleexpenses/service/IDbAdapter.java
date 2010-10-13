package com.app.settleexpenses.service;

import android.database.Cursor;
import android.database.SQLException;

import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Expense;

public interface IDbAdapter {

	IDbAdapter open() throws SQLException;

	IDbAdapter openWritable() throws SQLException;

	boolean close();

	long createOrUpdateEvent(long eventId, String title);

	boolean deleteEvent(long rowId);

	Cursor fetchAllEvents();

	Event getEventById(long eventId);

	long createExpense(Expense expense);

}