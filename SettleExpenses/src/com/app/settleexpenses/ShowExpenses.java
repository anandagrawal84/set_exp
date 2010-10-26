package com.app.settleexpenses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Expense;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.handler.ActionHandler;
import com.app.settleexpenses.handler.ActivityTransitionActionHandler;
import com.app.settleexpenses.handler.DeleteExpenseHandler;
import com.app.settleexpenses.service.DbAdapter;
import com.app.settleexpenses.service.IDbAdapter;
import com.app.settleexpenses.service.ServiceLocator;

public class ShowExpenses extends ListActivity {
	
	private static final int SHOW_SETTLEMENTS = 7;
	
    private static final String TITLE = "title";
    private static final String PAID_BY = "paid_by";
    private static final String AMOUNT = "amount";
    private static final String PARTICIPANTS = "participants";

    private long eventId;
    private Event event;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_expenses);

        eventId = getIntent().getLongExtra(DbAdapter.EVENT_ID, -1);
        IDbAdapter mDbHelper = ServiceLocator.getDbAdapter();
        event = mDbHelper.getEventById(eventId);
        
        View header = getLayoutInflater().inflate(R.layout.heading, getListView(), false);
        ((TextView)header.findViewById(R.id.heading)).setText(event.getTitle());
        getListView().addHeaderView(header);
        
        List<Expense> expenses = event.getExpenses();
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (Expense expense : expenses) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put(TITLE, expense.getTitle());
            item.put(PAID_BY, expense.getPaidBy().getName());
            item.put(AMOUNT, expense.getAmount() + "");
            item.put(PARTICIPANTS, flattenParticipants(expense.getParticipants()));
            list.add(item);
        }
        setListAdapter(new SimpleAdapter(this, list, R.layout.expense,
                new String[]{TITLE, PAID_BY, AMOUNT, PARTICIPANTS},
                new int[]{R.id.title, R.id.paid_by, R.id.amount, R.id.participants}));
    }

    private String flattenParticipants(ArrayList<Participant> participants) {
    	if (participants.size() == 0) return "";
        ArrayList<String> names = new ArrayList<String>();
        for (Participant participant : participants) {
            names.add(participant.getName());
        }
        return names.toString();
    }

    public boolean onAddEventClick(View view) {
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, final int position, final long id) {
        final CharSequence[] items = {getString(R.string.delete_expense)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Expenses");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Expense expense = event.getExpenses().get(Integer.parseInt(id + ""));
                ActionHandler handler = menuOptionHandler(eventId, expense.getId()).get(item);
                handler.execute();
            }
        });
        builder.create().show();
    }

    private HashMap<Integer, ActionHandler> menuOptionHandler(long eventId, long expenseId) {
        HashMap<Integer, ActionHandler> menuOptionResult = new HashMap<Integer, ActionHandler>();
        menuOptionResult.put(0, new DeleteExpenseHandler(this, expenseId, eventId));
        return menuOptionResult;
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, SHOW_SETTLEMENTS, 0, getString(R.string.show_settlements));
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        ActionHandler handler = menuOptionHandler().get(item.getItemId());
        if(handler == null) return false;
        handler.execute();
		return false;
	}

    private HashMap<Integer, ActionHandler> menuOptionHandler() {
        HashMap<Integer, ActionHandler> menuOptionResult = new HashMap<Integer, ActionHandler>();
        menuOptionResult.put(SHOW_SETTLEMENTS, new ActivityTransitionActionHandler(this, ShowSettlements.class, eventId));
        return menuOptionResult;
    }

}
