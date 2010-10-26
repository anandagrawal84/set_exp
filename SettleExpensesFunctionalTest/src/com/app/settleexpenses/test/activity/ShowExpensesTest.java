package com.app.settleexpenses.test.activity;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.app.settleexpenses.ShowExpenses;
import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Expense;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.service.IDbAdapter;
import com.app.settleexpenses.service.ServiceLocator;

public class ShowExpensesTest extends ActivityInstrumentationTestCase2<ShowExpenses>{

	public ShowExpensesTest() {
		super("com.app.settleexpenses", ShowExpenses.class);
	}
	
	public void testShouldDisplayExpensesInListViewAlongWithHeader() {
		IDbAdapter dbAdapterMock = PowerMock.createMock(IDbAdapter.class);
		Event event = eventWithOneExpense();
		EasyMock.expect(dbAdapterMock.getEventById(-1)).andReturn(event);
		PowerMock.replayAll();
		ServiceLocator.setDbAdapter(dbAdapterMock);
		ShowExpenses activity = getActivity();
		ListView list = (ListView)activity.findViewById(android.R.id.list);
		assertEquals(2, list.getChildCount());
		PowerMock.verifyAll();
	}

	private Event eventWithOneExpense() {
		List<Expense> expenses = new ArrayList<Expense>();
		expenses.add(new Expense("drinks", 1000, -1, new Participant("1", "Anand"), new ArrayList<Participant>()));
		Event event = new Event(1, "Outing", expenses);
		return event;
	}
}
