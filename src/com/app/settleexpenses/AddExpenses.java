package com.app.settleexpenses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddExpenses extends Activity {
	
	private EditText mTitleText;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expenses);

        mTitleText = (EditText) findViewById(R.id.title);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(DbAdapter.EVENT_TITLE);
            setTitle("Add Expense for " + title);
        }

        
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(DbAdapter.EVENT_TITLE, mTitleText.getText().toString());

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
	}
}
