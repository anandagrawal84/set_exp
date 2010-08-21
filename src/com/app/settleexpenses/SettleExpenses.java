package com.app.settleexpenses;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class SettleExpenses extends ListActivity {
	public static final int INSERT_ID = Menu.FIRST;
	private static final int ACTIVITY_CREATE=0;
	
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

        String[] from = new String[] { DbAdapter.EVENT_TITLE };
        int[] to = new int[] { R.id.text1 };
        
        SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.event_row, c, from, to);
        setListAdapter(notes);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (intent == null) {
    		fillData();
    		return;
    	}
        super.onActivityResult(requestCode, resultCode, intent);
    }

}