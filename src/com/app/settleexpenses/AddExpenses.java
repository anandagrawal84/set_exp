package com.app.settleexpenses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    protected boolean[] selections;
    private ArrayList<String> allParticipantNames;
    private List<Participant> allParticipants;

    private TextView participantsText;

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
        allParticipants = contacts.find(getIntent().getStringArrayListExtra(DbAdapter.PARTICIPANT_IDS));
        allParticipantNames = participantNames(allParticipants);

        selections = new boolean[allParticipants.size()];
        for (int i = 0; i< selections.length; i++ ){
            selections[i] = true;
        }

        participantsText = (TextView) findViewById(R.id.participants);
        updateParticipantList();


        Button editParticipantList = (Button) findViewById(R.id.edit_participants);
        editParticipantList.setOnClickListener(new ButtonClickHandler());

        final Spinner paidBy = (Spinner) findViewById(R.id.paid_by);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allParticipantNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paidBy.setAdapter(arrayAdapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (isInValid(expenseTitleText.getText().toString()) || isInValid(expenseAmount.getText().toString())) {
                    Toast toast = Toast.makeText(currentActivity, "Please complete the required fields", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                DbAdapter dbAdapter = new DbAdapter(view.getContext(), new ContactsAdapter(currentActivity));

                float amount = Float.parseFloat(expenseAmount.getText().toString());
                ArrayList<Participant> participants = selectedParticipants();

                Expense expense = new Expense(expenseTitleText.getText().toString(), amount,
                        eventId, allParticipants.get(paidBy.getSelectedItemPosition()), participants);

                dbAdapter.createExpense(expense);
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

    private boolean isInValid(String value) {
        return value != null && value.trim().length() == 0;
    }

    private ArrayList<String> participantNames(List<Participant> allParticipants) {
        ArrayList<String> result = new ArrayList<String>();
        for (Participant participant : allParticipants) {
            result.add(participant.getName());
        }
        return result;
    }

    private ArrayList<Participant> selectedParticipants() {
        ArrayList<Participant> participants = new ArrayList<Participant>();
        for (int i = 0; i < selections.length; i++) {
            if (selections[i]) {
                participants.add(allParticipants.get(i));
            }
        }
        return participants;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Log.d("addExpense", allParticipantNames.toString());
        String[] participants = allParticipantNames.toArray(new String[allParticipantNames.size()]);
        return new AlertDialog.Builder(this)
                .setTitle("Planets")
                .setMultiChoiceItems(participants, selections, new DialogInterface.OnMultiChoiceClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    }
                })
                .setPositiveButton("OK", new DialogButtonClickHandler())
                .create();
    }

    private void updateParticipantList() {
        String selectedParticipants = participantNames(selectedParticipants()).toString();
        participantsText.setText(selectedParticipants.substring(1, selectedParticipants.length() - 1));
    }

    public class DialogButtonClickHandler implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int clicked) {
            switch (clicked) {
                case DialogInterface.BUTTON_POSITIVE:
                    updateParticipantList();
                    break;
            }
        }
    }

    public class ButtonClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            showDialog(0);
        }
    }
}
