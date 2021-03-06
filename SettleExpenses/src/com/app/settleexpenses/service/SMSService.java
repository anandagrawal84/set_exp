package com.app.settleexpenses.service;

import android.telephony.SmsManager;
import com.app.settleexpenses.domain.*;

import java.util.ArrayList;
import java.util.List;

public class SMSService {

    public void send(Event event) {
        ArrayList<String> numbers = participantNumbers(event);
        for (String number : numbers) {
            sendSMS(number, smsText(event));
        }
    }

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

    private ArrayList<String> participantNumbers(Event event) {
        ArrayList<String> emailIds = new ArrayList<String>();
        for (Participant participant : event.getParticipants()) {
            for (Phone email : participant.getPhoneNumbers()) {
                emailIds.add(email.getNumber());
            }
        }
        return emailIds;
    }
}
