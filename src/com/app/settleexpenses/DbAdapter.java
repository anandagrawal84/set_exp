package com.app.settleexpenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Expense;
import com.app.settleexpenses.domain.Participant;

import java.util.ArrayList;

public class DbAdapter {

    public static String EVENT_TITLE = "title";
    public static String EVENT_ID = "_id";

    public static String EXPENSE_TITLE = "title";
    public static String EXPENSE_BY = "paid_by";
    public static String EXPENSE_AMOUNT = "amount";
    public static String EXPENSE_EVENT_ID = "event_id";

    public static String PARTICIPANT_IDS = "participant_ids";
    public static String PARTICIPANT_ID = "participant";
    public static String PARTICIPANT_EXPENSE_ID = "expense_id";

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
//            db.execSQL("DROP TABLE IF EXISTS events");
//            db.execSQL("DROP TABLE IF EXISTS expenses");
//            db.execSQL("DROP TABLE IF EXISTS participants");
            onCreate(db);
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
        initialValues.put(EVENT_TITLE, title);
        return mDb.insert("events", null, initialValues);
    }

    public boolean deleteEvent(long rowId) {
        return mDb.delete("events", EVENT_ID + " = " + rowId, null) > 0;
    }

    public Cursor fetchAllEvents() {
        return mDb.query("events", new String[]{EVENT_ID, EVENT_TITLE}, null, null, null, null, null);
    }

    public Event GetEventById(long eventId) {
        Cursor cursor = mDb.query("events", null, EVENT_ID + "=" + eventId, null, null, null, null);
        cursor.moveToFirst();
        return new Event(cursor.getInt(0), cursor.getString(1), getExpensesByEventId(eventId));
    }

    private ArrayList<Expense> getExpensesByEventId(long eventId) {
        ArrayList<Expense> expenses = new ArrayList<Expense>();
        Cursor cursor = mDb.query("expenses", null, EXPENSE_EVENT_ID + "=" + eventId, null, null, null, null);
        cursor.moveToFirst();
        do {
            Participant paidBy = new Participant(cursor.getString(1));
            ArrayList<Participant> participants = getParticipantsByExpenseId(cursor.getInt(0));
            expenses.add(new Expense(cursor.getString(4), cursor.getFloat(2), eventId, paidBy, participants));
        } while (cursor.moveToNext());
        return expenses;
    }

    private ArrayList<Participant> getParticipantsByExpenseId(long expenseId) {
        ArrayList<Participant> participants = new ArrayList<Participant>();
        Cursor cursor = mDb.query("participants", null, PARTICIPANT_EXPENSE_ID + "=" + expenseId, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                participants.add(new Participant(cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        return participants;
    }

    public long createExpense(Expense expense) {
        long expenseId = mDb.insert("expenses", null, expense.toContentValues());
        for (Participant participant : expense.getParticipants()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PARTICIPANT_EXPENSE_ID, expenseId);
            contentValues.put(PARTICIPANT_ID, participant.getId());
            mDb.insert("participants", null, contentValues);
        }
        return expenseId;
    }

}
