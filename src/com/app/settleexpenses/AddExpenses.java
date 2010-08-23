package com.app.settleexpenses;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.*;
import com.app.settleexpenses.domain.Expense;
import com.app.settleexpenses.domain.Participant;

import java.util.ArrayList;

public class AddExpenses extends Activity {

    private EditText expenseTitleText;
    private EditText expenseAmount;

    private final Activity currentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expenses);
        setTitle("Add Expenses");

        expenseTitleText = (EditText) findViewById(R.id.title);
        expenseAmount = (EditText) findViewById(R.id.amount);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        final long eventId = getIntent().getLongExtra(DbAdapter.EVENT_ID, -1);
        final ArrayList<String> allParticipantIds = getIntent().getStringArrayListExtra(DbAdapter.PARTICIPANT_IDS);

        final ListView participantSelectorView = (ListView) findViewById(R.id.participant_selector);
        participantSelectorView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, allParticipantIds));
        participantSelectorView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        participantSelectorView.setSelected(true);

        final Spinner paidBy = (Spinner) findViewById(R.id.paid_by);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allParticipantIds);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paidBy.setAdapter(arrayAdapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                DbAdapter dbAdapter = new DbAdapter(currentActivity);
                dbAdapter.open();

                float amount = Float.parseFloat(expenseAmount.getText().toString());
                ArrayList<Participant> participants = selectedParticipants(participantSelectorView, allParticipantIds);

                Expense expense = new Expense(expenseTitleText.getText().toString(), amount,
                        eventId, new Participant(allParticipantIds.get(paidBy.getSelectedItemPosition())), participants);

                dbAdapter.createExpense(expense);
                dbAdapter.close();
                Toast toast = Toast.makeText(currentActivity, "Expense Created Successfully.", 2);
                toast.show();
            }

        });
    }

    private ArrayList<Participant> selectedParticipants(ListView participantSelectorView, ArrayList<String> allParticipantIds) {
        ArrayList<Participant> participants = new ArrayList<Participant>();
        SparseBooleanArray checkedItemPositions = participantSelectorView.getCheckedItemPositions();
        for (int i = 0; i < checkedItemPositions.size(); i++) {
            if (checkedItemPositions.valueAt(i)) {
                participants.add(new Participant(allParticipantIds.get(i)));
            }
        }
        return participants;
    }
}
