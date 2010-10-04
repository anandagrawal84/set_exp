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
import com.app.settleexpenses.service.SMSService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendSMS extends ListActivity {

    protected static final String NAME = "NAME";
    protected static final String TYPE = "TYPE";
    protected static final String VALUE = "VALUE";

    protected Event event;
    private List<HashMap<String, String>> list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_select_container);
        DbAdapter mDbHelper = new DbAdapter(this, new ContactsAdapter(this));
        event = mDbHelper.getEventById(getIntent().getLongExtra(DbAdapter.EVENT_ID, -1));

        list = viewMap();
        setListAdapter(new SimpleAdapter(this, list, R.layout.multi_select_row,
                new String[]{NAME, TYPE, VALUE},
                new int[]{R.id.name, R.id.type, R.id.value}));
        getListView().setOnItemClickListener(onClickListener());
    }

    protected List<HashMap<String, String>> viewMap() {
        List<Participant> participants = event.getParticipants();
        list = new ArrayList<HashMap<String, String>>();
        for (Participant participant : participants) {
            for (Phone phone : participant.getPhoneNumbers()) {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put(NAME, participant.getName());
                item.put(TYPE, phone.getType());
                item.put(VALUE, phone.getNumber());
                list.add(item);
            }
        }
        return list;
    }

    protected OnItemClickListener onClickListener() {
         return new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String number = list.get(position).get(VALUE);
                new SMSService().send(number, event);
                Toast toast = Toast.makeText(view.getContext(), getString(R.string.sms_sent_successfully), 2 );
                toast.show();
            }

        };
    }
}

