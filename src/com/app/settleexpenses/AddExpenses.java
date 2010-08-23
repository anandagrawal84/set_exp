package com.app.settleexpenses;

import android.app.Activity;
import android.os.Bundle;
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

        ListView v = (ListView) findViewById(R.id.participant_selector);
        v.setAdapter(new ArrayAdapter<String>(this, R.layout.event_row, allParticipantIds));

        final Spinner paidBy = (Spinner) findViewById(R.id.paid_by);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allParticipantIds);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paidBy.setAdapter(arrayAdapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                DbAdapter dbAdapter = new DbAdapter(currentActivity);
                dbAdapter.open();

                float amount = Float.parseFloat(expenseAmount.getText().toString());
                ArrayList<Participant> participants = new ArrayList<Participant>();
                for (String participantId : allParticipantIds) {
                    participants.add(new Participant(participantId));
                }
                Expense expense = new Expense(expenseTitleText.getText().toString(), amount,
                        eventId, new Participant(allParticipantIds.get(paidBy.getSelectedItemPosition())), participants);

                dbAdapter.createExpense(expense);
                dbAdapter.close();
                Toast toast = Toast.makeText(currentActivity, "Expense Created Successfully.", 2);
                toast.show();
            }

        });
    }
}
