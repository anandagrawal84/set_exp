package com.app.settleexpenses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Settlement;

public class ShowSettlements extends ListActivity {
	
	private static final String PAYER = "payer";
    private static final String PAY_TO = "payTo";
    private static final String AMOUNT = "amount";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_settlements);
        DbAdapter mDbHelper = new DbAdapter(this, new ContactsAdapter(this));
        Event event = mDbHelper.getEventById(getIntent().getLongExtra(DbAdapter.EVENT_ID, -1));
        List<Settlement> settlements = event.calculateSettlements();
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (Settlement settlement : settlements) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put(PAYER, settlement.payer().getName());
            item.put(AMOUNT, settlement.getAmount() + "");
            item.put(PAY_TO, settlement.receiver().getName());
            list.add(item);
        }
        setListAdapter(new SimpleAdapter(this, list, R.layout.settlement,
                new String[]{PAYER, AMOUNT, PAY_TO},
                new int[]{R.id.payer, R.id.amount, R.id.payTo}));
    }
}
