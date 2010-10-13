package com.app.settleexpenses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Settlement;
import com.app.settleexpenses.handler.ActionHandler;
import com.app.settleexpenses.handler.ActivityTransitionActionHandler;
import com.app.settleexpenses.service.DbAdapter;
import com.app.settleexpenses.service.IDbAdapter;
import com.app.settleexpenses.service.ServiceLocator;

public class ShowSettlements extends ListActivity {

	private static final int SEND_SMS = 1;
	private static final int SEND_EMAIL = 5;
	private static final int EXIT = 2;

	private static final String PAYER = "payer";
    private static final String PAY_TO = "payTo";
    private static final String AMOUNT = "amount";

    private long eventId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_settlements);
        IDbAdapter mDbHelper = ServiceLocator.getDbAdapter();
        eventId = getIntent().getLongExtra(DbAdapter.EVENT_ID, -1);
        Event event = mDbHelper.getEventById(eventId);
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

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, SEND_SMS, 0, getString(R.string.send_sms));
        menu.add(0, SEND_EMAIL, 0, getString(R.string.send_email));
        menu.add(0, EXIT, 0, getString(R.string.exit));
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
        menuOptionResult.put(SEND_SMS, new ActivityTransitionActionHandler(this, SendSMS.class, eventId));
        menuOptionResult.put(SEND_EMAIL, new ActivityTransitionActionHandler(this, SendEmail.class, eventId));
        return menuOptionResult;
    }

}
