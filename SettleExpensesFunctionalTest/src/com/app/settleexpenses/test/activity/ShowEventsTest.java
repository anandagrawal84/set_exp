package com.app.settleexpenses.test.activity;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import android.database.AbstractCursor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.app.settleexpenses.ShowEvents;
import com.app.settleexpenses.service.IDbAdapter;
import com.app.settleexpenses.service.ServiceLocator;

public class ShowEventsTest extends ActivityInstrumentationTestCase2<ShowEvents> {

	public ShowEventsTest() {
		super("com.app.settleexpenses", ShowEvents.class);
	}
	
	public void testSomething() {
		IDbAdapter dbAdapterMock = PowerMock.createMock(IDbAdapter.class);
		EasyMock.expect(dbAdapterMock.open()).andReturn(dbAdapterMock);
		EasyMock.expect(dbAdapterMock.fetchAllEvents()).andReturn(new StubCursor(new String[] {"Delhi", "Pune"}));
		EasyMock.expect(dbAdapterMock.close()).andReturn(true);
		PowerMock.replayAll();
		ServiceLocator.setDbAdapter(dbAdapterMock);
		ShowEvents activity = getActivity();
		ListView list = (ListView)activity.findViewById(android.R.id.list);
		assertEquals(3, list.getChildCount());
		PowerMock.verifyAll();
	}
	
	class StubCursor extends AbstractCursor {

		private String[] values;
		
		public StubCursor(String[] values) {
			this.values = values;
		}

		@Override
		public String[] getColumnNames() {
			return new String[]{"_id", "name", "title"};
		}

		@Override
		public int getCount() {
			return values.length;
		}

		@Override
		public double getDouble(int column) {
			return 0;
		}

		@Override
		public float getFloat(int column) {
			return 0;
		}

		@Override
		public int getInt(int column) {
			return 0;
		}

		@Override
		public long getLong(int column) {
			return 0;
		}

		@Override
		public short getShort(int column) {
			return 0;
		}

		@Override
		public String getString(int column) {
			return values[0];
		}

		@Override
		public boolean isNull(int column) {
			return getString(0) == null;
		}
		
	}
}
