package com.app.settleexpenses.handler;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.app.settleexpenses.R;
import com.app.settleexpenses.service.DbAdapter;
import com.app.settleexpenses.service.IDbAdapter;
import com.app.settleexpenses.service.ServiceLocator;

public class DeleteExpenseHandler implements ActionHandler {

    private Activity currentActivity;
    private long expenseId;
    private long eventId;

    public DeleteExpenseHandler(Activity currentActivity, long expenseId, long eventId) {
        this.currentActivity = currentActivity;
        this.expenseId = expenseId;
        this.eventId = eventId;
    }

    public void execute() {
        IDbAdapter dbHelper = ServiceLocator.getDbAdapter();
        dbHelper.deleteExpense(expenseId);
        Toast.makeText(currentActivity.getApplicationContext(), currentActivity.getString(R.string.expense_deleted), Toast.LENGTH_SHORT).show();

        currentActivity.finish();
        currentActivity.startActivity(currentActivity.getIntent());
    }
}
