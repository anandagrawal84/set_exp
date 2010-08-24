package com.app.settleexpenses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SettleExpenses extends ListActivity {
    public static final int INSERT_ID = Menu.FIRST;
    private static final int ACTIVITY_CREATE = 0;

    private Activity currentActivity = this;
    private DbAdapter mDbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_list);
        mDbHelper = new DbAdapter(this);
        mDbHelper.open();
        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                startActivityForResult(new Intent(this, CreateEvent.class), ACTIVITY_CREATE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillData() {
        Cursor c = mDbHelper.fetchAllEvents();
        startManagingCursor(c);

        String[] from = new String[]{DbAdapter.EVENT_TITLE};
        int[] to = new int[]{R.id.text1};

        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.event_row, c, from, to);
        setListAdapter(notes);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final CharSequence[] items = {"Show final settlements", "Delete Event"};
        final long eventId  = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Events");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Intent addExpensesIntent = new Intent(currentActivity, ShowSettlements.class);
                        addExpensesIntent.putExtra(DbAdapter.EVENT_ID, eventId);
                        startActivityForResult(addExpensesIntent, 1);
                        break;
                    case 1:
                        mDbHelper.deleteEvent(eventId);
                        Toast.makeText(getApplicationContext(), "Event is deleted", Toast.LENGTH_SHORT).show();
                        fillData();
                        break;
                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (intent == null) {
            fillData();
            return;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }

}