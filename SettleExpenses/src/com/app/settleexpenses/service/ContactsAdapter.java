package com.app.settleexpenses.service;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.app.settleexpenses.domain.Email;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.domain.Phone;


public class ContactsAdapter {

    private ContentResolver contentResolver;
    private Resources resources;

    public ContactsAdapter(ContentResolver contentResolver, Resources resources) {
        this.contentResolver = contentResolver;
        this.resources = resources;
    }

    public Participant find(String id) {
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + "=" + id, null, null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        Log.d("NUMBER", getPhoneNumber(id).toString());
		return new Participant(id, name, getEmailAddresses(id), getPhoneNumber(id));
    }
    
    private ArrayList<Phone> getPhoneNumber(String id) {
		ArrayList<Phone> phones = new ArrayList<Phone>();
 		
 		Cursor pCur = contentResolver.query(
 				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{id}, null);
 		while (pCur.moveToNext()) {
 			int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
 			String typeString = android.provider.ContactsContract.CommonDataKinds.Phone.getTypeLabel(resources, type, "").toString();
 			phones.add(new Phone(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)), 
 					typeString));
 
 		} 
 		pCur.close();
 		return phones;
    }
    
    public ArrayList<Email> getEmailAddresses(String id) {
 		ArrayList<Email> emails = new ArrayList<Email>();
 		
 		Cursor emailCur = contentResolver.query(
 				ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null); 
 		while (emailCur.moveToNext()) {
             int type = emailCur.getInt(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
             String typeString = android.provider.ContactsContract.CommonDataKinds.Email.getTypeLabel(resources, type, "").toString();
             Email email = new Email(emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)),
                     typeString);
 			emails.add(email);
 		} 
 		emailCur.close();
 		return(emails);
 	}
}
