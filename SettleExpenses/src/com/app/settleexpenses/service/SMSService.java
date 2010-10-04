package com.app.settleexpenses.service;

import android.telephony.SmsManager;
import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Settlement;

import java.util.List;

public class SMSService {
    public void send(String number, Event event) {
        sendSMS(number, smsText(event));
    }

    private String smsText(Event event) {
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
