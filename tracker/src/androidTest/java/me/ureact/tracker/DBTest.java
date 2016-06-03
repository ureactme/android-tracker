package me.ureact.tracker;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.ureact.tracker.db.Contract;
import me.ureact.tracker.db.DBHelper;

/**
 * Created by Valeriy Palamarchuk on 6/1/16
 */
public class DBTest extends ApplicationTestCase<Application> {
    private DBHelper db;

    public DBTest() {
        super(Application.class);
    }

    @Override
    public final void setUp() throws Exception {
        super.setUp();
        createApplication();
        db = new DBHelper(getContext());
    }

    public final void tearDown() {
        db.getWritableDatabase().delete(Contract.EventEntry.TABLE_NAME, null, null);
    }

    final public void testAddAndGet() throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "2");
        Date now = new Date();
        Event event = new Event("1", "2", "3", "4", 0.1, now, map);
        db.addEvent(event);

        Event events = db.getEvents().get(0);
        assertEquals("1", events.getCategory());
        assertEquals("2", events.getAction());
        assertEquals("3", events.getLabel());
        assertEquals("4", events.getGuid());
        assertEquals(0.1, events.getValue());
        assertEquals(now.getTime(), events.getDate().getTime());
        assertEquals("2", events.getTags().get("1"));
    }

    final public void testDelete() throws Exception {
        final Event event1 = new Event();
        Event event2 = new Event();
        db.addEvent(event1);
        db.addEvent(event2);


        ArrayList<String> list = new ArrayList<>();
        list.add(event1.getGuid());
        db.deleteEvents(list);

        ArrayList<Event> events = db.getEvents();
        assertEquals(1, events.size());
        assertEquals(event2.getGuid(), events.get(0).getGuid());
    }
}
