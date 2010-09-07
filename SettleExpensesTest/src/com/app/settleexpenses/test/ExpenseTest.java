package com.app.settleexpenses.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import com.app.settleexpenses.domain.Expense;
import com.app.settleexpenses.domain.Participant;

public class ExpenseTest extends TestCase {
    
    public void testReturnNegativeContributionAmountOfAParticipantWhoIsSupposedToPay() {
    	Participant paidBy = new Participant("1", "Anand");
    	Participant participant = new Participant("3", "Participant1");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(paidBy);
		participants.add(participant);
		Expense expense = new Expense("Expense 1", 300, 10, paidBy, participants);
		assertEquals(-150.0, expense.contributionAmount(participant));
    }
    
    public void testReturnPositiveContributionAmountOfAParticipantWhoIsSupposedToGet() {
    	Participant paidBy = new Participant("1", "Anand");
    	Participant participant = new Participant("3", "Participant1");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(paidBy);
		participants.add(participant);
		Expense expense = new Expense("Expense 1", 300, 10, paidBy, participants);
		assertEquals(150.0, expense.contributionAmount(paidBy));
    }
    
    public void testReturnAllAmountAsNegativeForParticipantWhenThereIsOnlyOneParticipantAndPayerIsNotThePartOfExpense() {
    	Participant paidBy = new Participant("1", "Anand");
    	Participant participant = new Participant("3", "Participant1");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(participant);
		Expense expense = new Expense("Expense 1", 300, 10, paidBy, participants);
		assertEquals(-300.0, expense.contributionAmount(participant));
    }
    
    public void testReturnAllAmountAsPositiveWhenAndPayerIsNotThePartOfExpense() {
    	Participant paidBy = new Participant("1", "Anand");
    	Participant participant = new Participant("3", "Participant1");
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(participant);
		Expense expense = new Expense("Expense 1", 300, 10, paidBy, participants);
		assertEquals(300.0, expense.contributionAmount(paidBy));
    }
}
