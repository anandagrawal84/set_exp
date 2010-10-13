package com.app.settleexpenses.service;

import android.app.Application;

public class ServiceLocator extends Application {
    private static DbAdapter dbAdapter;
    private static ContactsAdapter contactsAdapter;

    @Override
    public void onCreate() {
        if (contactsAdapter == null) {
            new ContactsAdapter(getContentResolver(), getResources());
        }

        if (dbAdapter == null) {
            new DbAdapter(getApplicationContext(), contactsAdapter);
        }
    }

    public static DbAdapter getDbAdapter() {
        return dbAdapter;
    }

    public static ContactsAdapter getContactsAdapter() {
        return contactsAdapter;
    }
}
