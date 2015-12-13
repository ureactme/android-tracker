package us.utrak.tracker;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.HashMap;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BasicUsageTest extends ApplicationTestCase<Application> {
    public BasicUsageTest() {
        super(Application.class);
    }

    final public void testInitializeUTracker() throws Exception {
        UTracker tracker = UTrakUs.getInstance(getApplication()).getTracker("token1", "user1");
        assertEquals(tracker.getToken(), "token1");
        assertEquals(tracker.getUser(), "user1");
    }

    final public void testCreateEvent() {
        Event e = new Event()
                    .setMetric("redbutton_click")
                    .setValue(1)
                    .addTag("SO:Name", "Android")
                    .addTag("SO:Version", "5.1");

        assertEquals(e.getMetric(), "redbutton_click");
        assertEquals(e.getValue(), 1.0);

        HashMap<String, String> tags = e.getTags();
        assertEquals(tags.size(), 2);
        assertEquals(tags.get("SO:Name"), "Android");
        assertEquals(tags.get("SO:Version"), "5.1");
    }
}