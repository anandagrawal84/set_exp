package com.app.settleexpenses;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.app.settleexpenses.handler.ActionHandler;
import com.app.settleexpenses.handler.ActivityTransitionActionHandler;
import com.app.settleexpenses.handler.DeleteEventActionHandler;
import com.app.settleexpenses.service.DbAdapter;
import com.app.settleexpenses.service.IDbAdapter;
import com.app.settleexpenses.service.ServiceLocator;

public class ShowEvents extends ListActivity {
    private static final int ACTIVITY_CREATE = 0;

    private IDbAdapter mDbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_list);

        View header = getLayoutInflater().inflate(R.layout.event_add_button, getListView(), false);
        getListView().addHeaderView(header);

        mDbHelper = ServiceLocator.getDbAdapter();
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
                ActionHandler handler = menuOptionHandler(eventId).get(item);
                handler.execute();

                if (item == 3) {
                    fillData();
                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private HashMap<Integer, ActionHandler> menuOptionHandler(long eventId) {
        HashMap<Integer, ActionHandler> menuOptionResult = new HashMap<Integer, ActionHandler>();
        menuOptionResult.put(0, new ActivityTransitionActionHandler(this, ShowSettlements.class, eventId));
        menuOptionResult.put(1, new ActivityTransitionActionHandler(this, CreateEvent.class, eventId));
        menuOptionResult.put(2, new ActivityTransitionActionHandler(this, ShowExpenses.class, eventId));
        menuOptionResult.put(3, new DeleteEventActionHandler(this, eventId));
        return menuOptionResult;
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