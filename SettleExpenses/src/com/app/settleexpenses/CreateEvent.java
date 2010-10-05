package com.app.settleexpenses;

import com.app.settleexpenses.service.DbAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CreateEvent extends Activity {

    private EditText titleText;
    private static final int ACTIVITY_ADD_EXPENSE = 0;
    private final Activity currentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        setTitle(getString(R.string.create_event));

        titleText = (EditText) findViewById(R.id.title);
        final long eventId = getIntent().getLongExtra(DbAdapter.EVENT_ID, -1);
        titleText.setText(eventTitle(eventId));

        Button confirmButton = (Button) findViewById(R.id.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String title = titleText.getText().toString();
                if (title != null && title.trim().length() == 0) {
                    Toast toast = Toast.makeText(currentActivity, getString(R.string.no_event_name_validation), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                DbAdapter dbAdapter = new DbAdapter(currentActivity, new ContactsAdapter(currentActivity));

                long newEventId = dbAdapter.createOrUpdateEvent(eventId, title);

                Intent intent = new Intent(currentActivity, AddExpenses.class);
                intent.putExtra(DbAdapter.EVENT_ID, newEventId);
                startActivityForResult(intent, ACTIVITY_ADD_EXPENSE);
            }

        });
    }

    private String eventTitle(long eventId) {
        DbAdapter dbAdapter = new DbAdapter(currentActivity, new ContactsAdapter(currentActivity));
        String title = "";
        if(eventId != -1) {
            title = dbAdapter.getEventById(eventId).getTitle();
        }
        return title;
    }
}
