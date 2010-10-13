package com.app.settleexpenses.handler;

import android.widget.Toast;

import com.app.settleexpenses.R;
import com.app.settleexpenses.ShowEvents;
import com.app.settleexpenses.service.IDbAdapter;
import com.app.settleexpenses.service.ServiceLocator;

public class DeleteEventActionHandler implements ActionHandler {

    private ShowEvents currentActivity;
    private long eventId;

    public DeleteEventActionHandler(ShowEvents currentActivity, long eventId) {
        this.currentActivity = currentActivity;
        this.eventId = eventId;
    }


    public void execute() {
        IDbAdapter dbHelper = ServiceLocator.getDbAdapter();
        dbHelper.deleteEvent(eventId);
        Toast.makeText(currentActivity.getApplicationContext(), currentActivity.getString(R.string.event_deleted), Toast.LENGTH_SHORT).show();
    }
}
