package com.app.settleexpenses;

import com.app.settleexpenses.service.ContactsAdapter;
import com.app.settleexpenses.service.DbAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ShowEvents extends ListActivity {
    private static final int ACTIVITY_CREATE = 0;

    private Activity currentActivity = this;
    private DbAdapter mDbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_list);

        View header = getLayoutInflater().inflate(R.layout.event_add_button, getListView(), false);
        getListView().addHeaderView(header);

        mDbHelper = new DbAdapter(this, new ContactsAdapter(this));
        fillData();
    }

    public boolean onAddEventClick(View view) {
        startActivityForResult(new Intent(this, CreateEvent.class), ACTIVITY_CREATE);
        return true;
    }

    private void fillData() {
    	mDbHelper.open();
        Cursor c = mDbHelper.fetchAllEvents();
        startManagingCursor(c);

        String[] from = new String[]{DbAdapter.EVENT_TITLE};
        int[] to = new int[]{R.id.text1};

        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.event_row, c, from, to);
        setListAdapter(notes);
        mDbHelper.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final CharSequence[] items = {getString(R.string.show_settlements), getString(R.string.edit_event),
                getString(R.string.view_expenses), getString(R.string.delete_event)};
        final long eventId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Events");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Intent showSettlementIntent = new Intent(currentActivity, ShowSettlements.class);
                        showSettlementIntent.putExtra(DbAdapter.EVENT_ID, eventId);
                        startActivityForResult(showSettlementIntent, 1);
                        break;
                    case 1:
                        Intent editEventIntent = new Intent(currentActivity, CreateEvent.class);
                        editEventIntent.putExtra(DbAdapter.EVENT_ID, eventId);
                        startActivityForResult(editEventIntent, ACTIVITY_CREATE);
                        break;
                    case 2:
                        Intent showExpensesIntent = new Intent(currentActivity, ShowExpenses.class);
                        showExpensesIntent.putExtra(DbAdapter.EVENT_ID, eventId);
                        startActivityForResult(showExpensesIntent, ACTIVITY_CREATE);
                        break;
                    case 3:
                        mDbHelper.deleteEvent(eventId);
                        Toast.makeText(getApplicationContext(), getString(R.string.event_deleted), Toast.LENGTH_SHORT).show();
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
    }

}