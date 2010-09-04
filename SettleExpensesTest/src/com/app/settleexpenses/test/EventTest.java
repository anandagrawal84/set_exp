package com.app.settleexpenses.test;

import java.util.ArrayList;
import java.util.List;

import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Expense;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.domain.Settlement;

import junit.framework.TestCase;

public class EventTest extends TestCase {

	public void testShouldReturnAllTheParticipantsInTheEvent() {
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Participant p1 = new Participant("12", "asdf");
		Participant p2 = new Participant("134", "asdf");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(p1);
		participants.add(p2);
		expenses.add(new Expense("1", 300, 1, p1, participants));
		expenses.add(new Expense("1", 300, 1, p2, participants));
		Event event = new Event(1, "Title", expenses);
		assertEquals(2, event.getParticipants().size());
	}
	
	public void testShouldCalculateSettlementsWhenSettlemetsNullifies() {
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Participant p1 = new Participant("12", "asdf");
		Participant p2 = new Participant("134", "asdf");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(p1);
		participants.add(p2);
		expenses.add(new Expense("1", 300, 1, p1, participants));
		expenses.add(new Expense("1", 300, 1, p2, participants));
		Event event = new Event(1, "Title", expenses);
		assertEquals(0, event.calculateSettlements().size());
	}
	
	public void testShouldCalculateSettlementsWhenThereIsOnlyOneExpenseAndPayerIsAParticipant() {
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Participant p1 = new Participant("12", "asdf");
		Participant p2 = new Participant("134", "asdf");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(p1);
		participants.add(p2);
		expenses.add(new Expense("1", 300, 1, p1, participants));
		Event event = new Event(1, "Title", expenses);
		List<Settlement> settlements = event.calculateSettlements();
		assertEquals(1, settlements.size());
		assertEquals(p2.getId(), settlements.get(0).payer().getId());
		assertEquals(p1.getId(), settlements.get(0).receiver().getId());
		assertEquals(150F, settlements.get(0).getAmount());
	}
	
	public void testShouldCalculateSettlementsWhenThereIsOnlyOneExpenseAndPayerIsNotAParticipant() {
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Participant p1 = new Participant("12", "asdf");
		Participant p2 = new Participant("134", "asdf");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(p2);
		expenses.add(new Expense("1", 300, 1, p1, participants));
		Event event = new Event(1, "Title", expenses);
		List<Settlement> settlements = event.calculateSettlements();
		assertEquals(1, settlements.size());
		assertEquals(p2.getId(), settlements.get(0).payer().getId());
		assertEquals(p1.getId(), settlements.get(0).receiver().getId());
		assertEquals(300F, settlements.get(0).getAmount());
	}
}
