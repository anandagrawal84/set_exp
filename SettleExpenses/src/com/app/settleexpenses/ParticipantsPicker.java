package com.app.settleexpenses;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.app.settleexpenses.domain.Participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsPicker extends Activity {

    private static final int PICK_CONTACT = 0;
    private ArrayAdapter<String> contactsAdapter;
    private final ArrayList<String> contactIds = new ArrayList<String>();
    private final Activity currentActivity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participants_picker);
        setTitle("Pick Participants");

        final long eventId = getIntent().getLongExtra(DbAdapter.EVENT_ID, -1);
        contactsAdapter = new ArrayAdapter<String>(this, R.layout.participant, new ArrayList<String>());

        populateParticipants(eventId);

        ListView v = (ListView) findViewById(R.id.list);
        v.setAdapter(contactsAdapter);

        Button addParticipantButton = (Button) findViewById(R.id.add_participant);
        Button continueButton = (Button) findViewById(R.id.continueButton);
        addParticipantButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        continueButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (contactIds.size() < 2) {
                    Toast toast = Toast.makeText(currentActivity, "Please add participants", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Intent addExpensesIntent = new Intent(currentActivity, AddExpenses.class);
                addExpensesIntent.putExtra(DbAdapter.PARTICIPANT_IDS, contactIds);
                addExpensesIntent.putExtra(DbAdapter.EVENT_ID, eventId);
                startActivityForResult(addExpensesIntent, 1);
            }
        });
    }

    private void populateParticipants(long eventId) {
        DbAdapter dbAdapter = new DbAdapter(currentActivity, new ContactsAdapter(currentActivity));
        List<Participant> participants = dbAdapter.getEventById(eventId).getParticipants();
        for(Participant participant : participants) {
            contactIds.add(participant.getId());
            contactsAdapter.add(participant.getName());
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        if (contactIds.contains(id)){
	                        Toast toast = Toast.makeText(currentActivity, "Participant is already in the list", Toast.LENGTH_LONG);
	                        toast.show();
                        }else{
                        	contactsAdapter.add(name);
                            contactIds.add(id);
                        }
                    }
                }
                break;
        }
    }
}
