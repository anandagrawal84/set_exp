package com.app.settleexpenses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.SimpleAdapter;

import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.domain.Settlement;

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
        DbAdapter mDbHelper = new DbAdapter(this, new ContactsAdapter(this));
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
    	menu.add(0,SEND_SMS,0,"Send SMS");
    	menu.add(0,SEND_EMAIL,0,"Send EMAIL");
    	menu.add(0,EXIT,0,"Exit");
    	return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case SEND_SMS:
                Intent sendSmsIntent = new Intent(this, SendSMS.class);
                sendSmsIntent.putExtra(DbAdapter.EVENT_ID, eventId);
                startActivityForResult(sendSmsIntent, 1);
                break;
            case SEND_EMAIL:
                Intent sendEmailIntent = new Intent(this, SendEmail.class);
                sendEmailIntent.putExtra(DbAdapter.EVENT_ID, eventId);
                startActivityForResult(sendEmailIntent, 1);
                break;
		}
		return false;
	}

}
