package com.app.settleexpenses.test.functional;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;

import com.app.settleexpenses.ShowEvents;
import com.jayway.android.robotium.solo.Solo;

public class EndToEndTest extends ActivityInstrumentationTestCase2<ShowEvents>{
	
	private Solo solo;
	
	public EndToEndTest() {
		super("com.app.settleexpenses", ShowEvents.class);
	}
	
	public void setUp() {
		solo = new Solo(getInstrumentation(), getActivity());
		new ContactsBuilder(getActivity()).addNewContact("Anand", 1, "123456", "email", "a@a.com", 1);
	}
	
	@Smoke
	public void testX() {
		solo.clickOnText("Add an event");
	}
	
	public void tearDown() throws Exception{
		try {
			solo.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();	
	}

}
