package com.app.settleexpenses;

import android.app.ListActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.domain.Phone;
import com.app.settleexpenses.domain.Settlement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendSMS extends ListActivity {

    private static final String NAME = "NAME";
    private static final String TYPE = "TYPE";
    private static final String VALUE = "VALUE";

    private Event event;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_select_container);
        DbAdapter mDbHelper = new DbAdapter(this, new ContactsAdapter(this));
        event = mDbHelper.getEventById(getIntent().getLongExtra(DbAdapter.EVENT_ID, -1));

        List<Participant> settlements = event.getParticipants();
        final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (Participant participant : settlements) {
            for (Phone phone : participant.getPhoneNumbers()) {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put(NAME, participant.getName());
                item.put(TYPE, phone.getType());
                item.put(VALUE, phone.getNumber());
                list.add(item);
            }
        }
        setListAdapter(new SimpleAdapter(this, list, R.layout.multi_select_row,
                new String[]{NAME, TYPE, VALUE},
                new int[]{R.id.name, R.id.type, R.id.value}));
        getListView().setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String number = list.get(position).get(VALUE);
                sendSMS(number, smsText());
                Toast toast = Toast.makeText(view.getContext(), "SMS sent to contact successfully.", 2 );
                toast.show();
            }

        });
    }


    private String smsText() {
        String result = "Here are the settlements for : " + event.getTitle();
        List<Settlement> settlements = event.calculateSettlements();
        for(Settlement settlement : settlements){
            result += ("\n" + settlement.payer().getName() + " pays " + settlement.getAmount() + " to " + settlement.receiver().getName());
        }
        return result;
    }


    private void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message), null, null);
    }

}

