package com.app.settleexpenses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.app.settleexpenses.domain.Email;
import com.app.settleexpenses.domain.Participant;
import com.app.settleexpenses.service.EmailService;

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

    protected View.OnClickListener sendToAllClickListener() {
        return new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new EmailService().emailIntent(event);
                startActivity(Intent.createChooser(intent, "Send mail..."));
            }
        };
    }

    protected AdapterView.OnItemClickListener onClickListener() {
        return new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String email = list.get(position).get(VALUE);
                Intent intent = new EmailService().emailIntent(email, event);
                startActivity(Intent.createChooser(intent, "Send mail..."));
            }

        };
    }
}
