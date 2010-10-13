package com.app.settleexpenses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.domain.Phone;
import com.app.settleexpenses.service.ContactsAdapter;
import com.app.settleexpenses.service.DbAdapter;
import com.app.settleexpenses.service.SMSService;
import com.app.settleexpenses.service.ServiceLocator;

public class SendSMS extends ListActivity {

    protected static final String NAME = "NAME";
    protected static final String TYPE = "TYPE";
    protected static final String VALUE = "VALUE";

    protected Event event;
    private List<HashMap<String, String>> list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_select_container);
        DbAdapter mDbHelper = ServiceLocator.getDbAdapter();
        event = mDbHelper.getEventById(getIntent().getLongExtra(DbAdapter.EVENT_ID, -1));

        list = viewMap();
        setListAdapter(new SimpleAdapter(this, list, R.layout.multi_select_row,
                new String[]{NAME, TYPE, VALUE},
                new int[]{R.id.name, R.id.type, R.id.value}));
        getListView().setOnItemClickListener(onClickListener());
        Button sendToAll = (Button)findViewById(R.id.send_to_all);
        sendToAll.setOnClickListener(sendToAllClickListener());
    }

    protected View.OnClickListener sendToAllClickListener() {
        return new View.OnClickListener(){
            public void onClick(View view) {
                new SMSService().send(event);
                showInformationMessage(getString(R.string.sms_sent_to_all));
            }
        };
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
                showInformationMessage(getString(R.string.sms_sent_successfully));
            }

        };
    }

    private void showInformationMessage(String message) {
        Toast toast = Toast.makeText(this.getApplicationContext(), message, 2 );
                toast.show();
    }
}

