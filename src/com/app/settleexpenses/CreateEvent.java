package com.app.settleexpenses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CreateEvent extends Activity {

	private EditText titleText;
	private static final int ACTIVITY_ADD_EXPENSE=0;
	private final Activity currentActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        setTitle("Create Event");

        titleText = (EditText) findViewById(R.id.title);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String title = titleText.getText().toString();
                if(title!= null && title.trim().length() == 0) {
                    Toast toast = Toast.makeText(currentActivity, "Please enter event name", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                DbAdapter dbAdapter = new DbAdapter(currentActivity, new ContactsAdapter(currentActivity));
                dbAdapter.open();

				long eventId = dbAdapter.createEvent(title);
                
                Intent intent = new Intent(currentActivity, ParticipantsPicker.class);
                intent.putExtra(DbAdapter.EVENT_ID, eventId);
                dbAdapter.close();
				startActivityForResult(intent, ACTIVITY_ADD_EXPENSE);
            }

        });
    }
}
