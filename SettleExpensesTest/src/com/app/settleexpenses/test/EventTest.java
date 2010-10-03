package com.app.settleexpenses.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Expense;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.domain.Settlement;

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
		assertEquals(150D, settlements.get(0).getAmount());
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
		assertEquals(300D, settlements.get(0).getAmount());
	}
	
	public void testShouldCalculateOurJaipurExpenses() {
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Participant anand = new Participant("12", "Anand");
		Participant sunit = new Participant("134", "Sunit");
		Participant chandan = new Participant("14", "Chandan");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(anand);
		participants.add(sunit);
		participants.add(chandan);
		expenses.add(new Expense("lunch", 600, 1, sunit, participants));
		expenses.add(new Expense("chokhi dhani", 1050, 1, sunit, participants));
		expenses.add(new Expense("cab", 1800, 1, sunit, participants));
		expenses.add(new Expense("cab", 1000, 1, anand, participants));
		expenses.add(new Expense("cab", 1000, 1, chandan, participants));
		expenses.add(new Expense("ge", 200, 1, anand, participants));
		expenses.add(new Expense("ge", 600, 1, chandan, participants));
		expenses.add(new Expense("ge", 500, 1, sunit, participants));
		
		Event event = new Event(1, "Title", expenses);
		List<Settlement> settlements = event.calculateSettlements();
		assertEquals(2, settlements.size());
		
		assertEquals(anand.getId(), settlements.get(0).payer().getId());
		assertEquals(sunit.getId(), settlements.get(0).receiver().getId());
		assertEquals(1050D, settlements.get(0).getAmount());
		
		assertEquals(chandan.getId(), settlements.get(1).payer().getId());
		assertEquals(sunit.getId(), settlements.get(1).receiver().getId());
		assertEquals(650D, settlements.get(1).getAmount());
	}
	
	public void testShouldCalculateOurDelhiExpenses() {
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Participant anand = new Participant("12", "Anand");
		Participant sapto = new Participant("134", "sapto");
		Participant selva = new Participant("144", "selva");
		Participant chandan = new Participant("14", "Chandan");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(anand);
		participants.add(selva);
		participants.add(chandan);
		expenses.add(new Expense("lunch", 185, 1, chandan, participants));
		expenses.add(new Expense("lunch", 160, 1, selva, participants));
		
		participants = new ArrayList<Participant>();
		participants.add(anand);
		participants.add(selva);
		participants.add(chandan);
		participants.add(sapto);
		expenses.add(new Expense("cab", 205, 1, sapto, participants));
		expenses.add(new Expense("cab", 250, 1, sapto, participants));
		
		participants = new ArrayList<Participant>();
		participants.add(selva);
		participants.add(chandan);
		participants.add(sapto);
		expenses.add(new Expense("dinner", 855, 1, sapto, participants));
		
		Event event = new Event(1, "Title", expenses);
		List<Settlement> settlements = event.calculateSettlements();
		assertEquals(3, settlements.size());
		
		assertEquals(selva.getId(), settlements.get(0).payer().getId());
		assertEquals(sapto.getId(), settlements.get(0).receiver().getId());
		assertEquals(353.75, settlements.get(0).getAmount());
		
		assertEquals(chandan.getId(), settlements.get(1).payer().getId());
		assertEquals(sapto.getId(), settlements.get(1).receiver().getId());
		assertEquals(328.75, settlements.get(1).getAmount());
		
		assertEquals(anand.getId(), settlements.get(2).payer().getId());
		assertEquals(sapto.getId(), settlements.get(2).receiver().getId());
		assertEquals(228.75, settlements.get(2).getAmount());
	}
	
	public void testShouldCalculateCorrectSettlementWhenAllExpensesHaveDifferentParticipants() {
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Participant anand = new Participant("12", "Anand");
		Participant sapto = new Participant("134", "sapto");
		Participant selva = new Participant("144", "selva");
		
		expenses.add(new Expense("lunch", 500, 1, sapto, new ArrayList<Participant>(Arrays.asList(new Participant[] {anand, selva}))));
		
		Participant ankit = new Participant("12adf", "ankit");
		Participant abhas = new Participant("1343", "abhas");
		Participant arti = new Participant("1442", "arti");
		Participant amit = new Participant("1441", "amit");
		
		expenses.add(new Expense("lunch", 1000, 1, ankit, new ArrayList<Participant>(Arrays.asList(new Participant[] {abhas, arti, amit}))));
		
		Event event = new Event(1, "Title", expenses);
		List<Settlement> settlements = event.calculateSettlements();
		assertEquals(5, settlements.size());
		
		assertEquals(arti.getId(), settlements.get(0).payer().getId());
		assertEquals(ankit.getId(), settlements.get(0).receiver().getId());
		assertEquals(333.33, settlements.get(0).getAmount());
		
		
		assertEquals(abhas.getId(), settlements.get(1).payer().getId());
		assertEquals(ankit.getId(), settlements.get(1).receiver().getId());
		assertEquals(333.33, settlements.get(1).getAmount());
		
		assertEquals(amit.getId(), settlements.get(2).payer().getId());
		assertEquals(ankit.getId(), settlements.get(2).receiver().getId());
		assertEquals(333.33, settlements.get(2).getAmount());
		
		assertEquals(selva.getId(), settlements.get(3).payer().getId());
		assertEquals(sapto.getId(), settlements.get(3).receiver().getId());
		assertEquals(250.0, settlements.get(3).getAmount());
		
		assertEquals(anand.getId(), settlements.get(4).payer().getId());
		assertEquals(sapto.getId(), settlements.get(4).receiver().getId());
		assertEquals(250.0, settlements.get(4).getAmount());
	}
	
	public void testShouldReturnCorrectSettlementsIfSettlementsAreInFraction() {
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Participant anand = new Participant("12", "Anand");
		Participant sapto = new Participant("134", "sapto");
		Participant selva = new Participant("144", "selva");
		
		expenses.add(new Expense("lunch", 500, 1, anand, new ArrayList<Participant>(Arrays.asList(new Participant[] {anand, sapto, selva}))));
		Event event = new Event(1, "Title", expenses);
		List<Settlement> settlements = event.calculateSettlements();
		
		assertEquals(selva.getId(), settlements.get(0).payer().getId());
		assertEquals(anand.getId(), settlements.get(0).receiver().getId());
		assertEquals(166.67, settlements.get(0).getAmount());
		
		
		assertEquals(sapto.getId(), settlements.get(1).payer().getId());
		assertEquals(anand.getId(), settlements.get(1).receiver().getId());
		assertEquals(166.67, settlements.get(1).getAmount());
		
	}
}
