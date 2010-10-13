package com.app.settleexpenses.test.functional;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Contacts.ContactMethods;

public class ContactsBuilder {
	
	private Activity activity;
	
	public ContactsBuilder(Activity activity) {
		super();
		this.activity = activity;
	}

	public void addNewContact(String name, int phoneType, String number, String label,
			String email, int emailType){
		ContentValues values = new ContentValues();
		Uri phoneUri = null;
		Uri emailUri = null;

		values.put(Contacts.People.NAME, name);

		//Add Phone Numbers
		Uri uri = activity.getContentResolver().insert(Contacts.People.CONTENT_URI, values);
		phoneUri = Uri.withAppendedPath(uri, Contacts.People.Phones.CONTENT_DIRECTORY);

		values.clear();
		values.put(Contacts.Phones.TYPE, 1);
		values.put(Contacts.Phones.NUMBER, number);
		values.put(Contacts.Phones.LABEL, label);
		activity.getContentResolver().insert(phoneUri, values);

		//Add Email
		emailUri = Uri.withAppendedPath(uri, android.provider.Contacts.People.ContactMethods.CONTENT_DIRECTORY);

		values.clear();
		values.put(ContactMethods.KIND, Contacts.KIND_EMAIL);
		values.put(ContactMethods.DATA, email);
		values.put(ContactMethods.LABEL, "");
		values.put(ContactMethods.TYPE, emailType);
		activity.getContentResolver().insert(emailUri, values);
	}
}
