package com.app.settleexpenses;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import com.app.settleexpenses.domain.Participant;

import java.util.ArrayList;
import java.util.List;


public class ContactsAdapter {

    private Activity activity;

    public ContactsAdapter(Activity activity) {
        this.activity = activity;
    }

    public Participant find(String id) {
        Cursor cursor = activity.managedQuery(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + "=" + id, null, null);
        cursor.moveToFirst();
        return new Participant(id, cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
    }

    public List<Participant> find(List<String> ids) {
        ArrayList<Participant> participants = new ArrayList<Participant>();
        for (String id : ids) {
            participants.add(find(id));
        }
        return participants;
    }
}
