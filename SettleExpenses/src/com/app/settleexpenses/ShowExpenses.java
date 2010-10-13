package com.app.settleexpenses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Expense;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.service.DbAdapter;
import com.app.settleexpenses.service.IDbAdapter;
import com.app.settleexpenses.service.ServiceLocator;

public class ShowExpenses extends ListActivity {

    private static final String TITLE = "title";
    private static final String PAID_BY = "paid_by";
    private static final String AMOUNT = "amount";
    private static final String PARTICIPANTS = "participants";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_expenses);

        IDbAdapter mDbHelper = ServiceLocator.getDbAdapter();
        Event event = mDbHelper.getEventById(getIntent().getLongExtra(DbAdapter.EVENT_ID, -1));
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
}
