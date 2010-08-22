package com.app.settleexpenses;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import com.app.settleexpenses.domain.Expense;

import java.util.ArrayList;

public class AddExpenses extends Activity {

    private EditText expenseTitleText;
    private EditText expenseAmount;

    private final Activity currentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expenses);

        expenseTitleText = (EditText) findViewById(R.id.title);
        expenseAmount = (EditText) findViewById(R.id.amount);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        final long eventId = getIntent().getLongExtra(DbAdapter.EVENT_ID, -1);
        final ArrayList<String> participantIds = getIntent().getStringArrayListExtra(ParticipantsPicker.PARTICIPANT_IDS);


        setTitle("Add Expenses");

        ListView v = (ListView) findViewById(R.id.participant_selector);
        v.setAdapter(new ArrayAdapter<String>(this, R.layout.event_row, participantIds));

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                DbAdapter dbAdapter = new DbAdapter(currentActivity);
                dbAdapter.open();

                float amount = Float.parseFloat(expenseAmount.getText().toString());
                Expense expense = new Expense(expenseTitleText.getText().toString(), amount,
                        eventId, null, null);
                dbAdapter.createExpense(expense);
            }

        });
    }
}
