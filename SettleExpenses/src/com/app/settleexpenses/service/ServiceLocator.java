package com.app.settleexpenses.service;

import android.app.Application;

public class ServiceLocator extends Application {
    private static IDbAdapter dbAdapter;

    @Override
    public void onCreate() {
        if (dbAdapter == null) {
        	ContactsAdapter contactsAdapter = new ContactsAdapter(getContentResolver(), getResources());
            dbAdapter = new DbAdapter(getApplicationContext(), contactsAdapter);
        }
    }

    public static IDbAdapter getDbAdapter() {
        return dbAdapter;
    }

	public static void setDbAdapter(IDbAdapter dbAdapter) {
		ServiceLocator.dbAdapter = dbAdapter;
	}
    
}
