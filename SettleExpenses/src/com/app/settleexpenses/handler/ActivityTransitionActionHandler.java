package com.app.settleexpenses.handler;

import android.app.Activity;
import android.content.Intent;
import com.app.settleexpenses.service.DbAdapter;

public class ActivityTransitionActionHandler implements ActionHandler {

    private Activity currentActivity;
    private Class targetActivity;
    private long eventId;

    public ActivityTransitionActionHandler(Activity currentActivity, Class targetActivity, long eventId) {
        this.currentActivity = currentActivity;
        this.targetActivity = targetActivity;
        this.eventId = eventId;
    }

    public void execute() {
        Intent editEventIntent = new Intent(currentActivity, targetActivity);
        editEventIntent.putExtra(DbAdapter.EVENT_ID, eventId);
        currentActivity.startActivityForResult(editEventIntent, 1);
    }
}
