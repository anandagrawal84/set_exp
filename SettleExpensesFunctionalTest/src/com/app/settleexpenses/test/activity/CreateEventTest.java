package com.app.settleexpenses.test.activity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.app.settleexpenses.CreateEvent;
import com.app.settleexpenses.R;
import com.app.settleexpenses.service.DbAdapter;

public class CreateEventTest extends ActivityInstrumentationTestCase2<CreateEvent>{
	
	public CreateEventTest() {
		super("com.app.settleexpenses", CreateEvent.class);
		Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra(DbAdapter.EVENT_ID, 2);
		setActivityIntent(intent);
	}
	
	public void testShouldDisplayCreateEventTitleWhenInCreateMode() {
		CreateEvent activity = getActivity();
		assertEquals(activity.getString(R.string.create_event), activity.getTitle());
	}

}
