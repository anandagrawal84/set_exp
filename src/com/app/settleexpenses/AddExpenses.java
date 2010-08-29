package com.app.settleexpenses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.*;
import com.app.settleexpenses.domain.Expense;
import com.app.settleexpenses.domain.Participant;

import java.util.ArrayList;
import java.util.List;

public class AddExpenses extends Activity {

    private EditText expenseTitleText;
    private EditText expenseAmount;

    private final Activity currentActivity = this;
    private final ContactsAdapter contacts = new ContactsAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expenses);
        setTitle("Add Expenses");

        expenseTitleText = (EditText) findViewById(R.id.title);
        expenseAmount = (EditText) findViewById(R.id.amount);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        Button calculateButton = (Button) findViewById(R.id.calculate);

        final long eventId = getIntent().getLongExtra(DbAdapter.EVENT_ID, -1);
        final List<Participant> allParticipants = contacts.find(getIntent().getStringArrayListExtra(DbAdapter.PARTICIPANT_IDS));
        final ArrayList<String> allParticipantNames = participantNames(allParticipants);

        final ListView participantSelectorView = (ListView) findViewById(R.id.participant_selector);
        participantSelectorView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, allParticipantNames));
        participantSelectorView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        participantSelectorView.setSelected(true);

        final Spinner paidBy = (Spinner) findViewById(R.id.paid_by);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allParticipantNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paidBy.setAdapter(arrayAdapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(isInValid(expenseTitleText.getText().toString()) || isInValid(expenseAmount.getText().toString())){
                    Toast toast = Toast.makeText(currentActivity, "Please complete the required fields", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                DbAdapter dbAdapter = new DbAdapter(view.getContext(), new ContactsAdapter(currentActivity));
                dbAdapter.open();

                float amount = Float.parseFloat(expenseAmount.getText().toString());
                ArrayList<Participant> participants = selectedParticipants(participantSelectorView, allParticipants);

                Expense expense = new Expense(expenseTitleText.getText().toString(), amount,
                        eventId, allParticipants.get(paidBy.getSelectedItemPosition()), participants);

                dbAdapter.createExpense(expense);
                dbAdapter.close();
                Toast toast = Toast.makeText(view.getContext(), "Expense Created Successfully.", 2);
                toast.show();
                finish();
                startActivity(currentActivity.getIntent());
            }

        });

        calculateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent addExpensesIntent = new Intent(view.getContext(), ShowSettlements.class);
                addExpensesIntent.putExtra(DbAdapter.EVENT_ID, eventId);
                startActivityForResult(addExpensesIntent, 1);
            }
        });
    }

    private boolean isInValid(String value){
        return value != null && value.trim().length() == 0;
    }

    private ArrayList<String> participantNames(List<Participant> allParticipants) {
        ArrayList<String> result = new ArrayList<String>();
        for (Participant participant : allParticipants) {
            result.add(participant.getName());
        }
        return result;
    }

    private ArrayList<Participant> selectedParticipants(ListView participantSelectorView, List<Participant> allParticipants) {
        ArrayList<Participant> participants = new ArrayList<Participant>();
        SparseBooleanArray checkedItemPositions = participantSelectorView.getCheckedItemPositions();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            if (checkedItemPositions.valueAt(i)) {
                participants.add(allParticipants.get(i));
            }
        }
        return participants;
    }
}
