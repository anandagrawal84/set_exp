package com.app.settleexpenses.service;

import android.content.Intent;
import com.app.settleexpenses.domain.Event;
import com.app.settleexpenses.domain.Settlement;

import java.util.List;

public class EmailService {
    public Intent emailIntent(String email, Event event) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new
                String[]{email});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Settlements for : " + event.getTitle());
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body(event));
        return emailIntent;
    }

    private String body(Event event) {
        String result = "";
        List<Settlement> settlements = event.calculateSettlements();
        for (Settlement settlement : settlements) {
            result += (settlement.payer().getName() + " pays " + settlement.getAmount() + " to " + settlement.receiver().getName() + "\n");
        }
        result += "\nRegards,\nTotalItUp Team";
        return result;
    }
}
