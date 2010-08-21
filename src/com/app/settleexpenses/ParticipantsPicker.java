package com.app.settleexpenses;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ParticipantsPicker extends Activity {

	private static final int PICK_CONTACT = 0;
	private ArrayList<String> contacts = new ArrayList<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.participants_picker);
        setTitle("Pick Participants");
        
        Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
		startActivityForResult(intent, PICK_CONTACT);
        
        Button addParticipantButton = (Button) findViewById(R.id.add_participant);
        addParticipantButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
			}
		});
    }
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	  switch (reqCode) {
	    case (PICK_CONTACT) :
	      if (resultCode == Activity.RESULT_OK) {
	        Uri contactData = data.getData();
	        Cursor c =  managedQuery(contactData, null, null, null, null);
	        if (c.moveToFirst()) {
	          String name = c.getString(c.getColumnIndexOrThrow(People.NAME));
	          ListView v = (ListView) findViewById(R.id.list);

	          contacts.add(name);
	          v.setAdapter(new ArrayAdapter<String>(this,R.layout.event_row , contacts));
	        }
	      }
	      break;
	  }
	}
}
