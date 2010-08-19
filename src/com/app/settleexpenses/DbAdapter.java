package com.app.settleexpenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter {

	public static String EVENT_TITLE = "title";
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		
		
	    private static final String DATABASE_NAME = "ShareExpenses";
	    private static final int DATABASE_VERSION = 2;
		
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table events (_id integer primary key autoincrement, "
        	        + "title text not null);");

            db.execSQL("create table expenses (_id integer primary key autoincrement, "
        	        + "paid_by text not null, amount decimal not null, event_id integer not null, " +
        	        		"title text not null);");
            db.execSQL("create table participants (expense_id integer, participant text not null);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

	private Context mCtx;
	private com.app.settleexpenses.DbAdapter.DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public long createEvent(String title) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("title", title);
        return mDb.insert("events", null, initialValues);
    }

    public boolean deleteEvent(long rowId) {
        return mDb.delete("events", "_id = " + rowId, null) > 0;
    }
    
    public Cursor fetchAllEvents() {
        return mDb.query("events", new String[] {"_id", "title"}, null, null, null, null, null);
    }

}
