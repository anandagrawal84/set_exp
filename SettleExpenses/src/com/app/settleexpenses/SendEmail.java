package com.app.settleexpenses;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.app.settleexpenses.domain.Email;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.service.EmailService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendEmail extends SendSMS {
    private List<HashMap<String, String>> list;

    protected List<HashMap<String, String>> viewMap() {
        List<Participant> participants = event.getParticipants();
        list = new ArrayList<HashMap<String, String>>();
        for (Participant participant : participants) {
            for (Email email : participant.getEmails()) {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put(NAME, participant.getName());
                item.put(TYPE, email.getType());
                item.put(VALUE, email.getEmail());
                list.add(item);
            }
        }
        return list;
    }

    protected AdapterView.OnItemClickListener onClickListener() {
        return new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String number = list.get(position).get(VALUE);
                Intent intent = new EmailService().emailIntent(number, event);
                startActivity(Intent.createChooser(intent, "Send mail..."));
            }

        };
    }
}
