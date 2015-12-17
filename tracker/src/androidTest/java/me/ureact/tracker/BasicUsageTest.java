package me.ureact.tracker;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BasicUsageTest extends ApplicationTestCase<Application> {
    public BasicUsageTest() {
        super(Application.class);
    }

    final public void testInitializeUTracker() throws Exception {
        UTracker tracker = UReactMe.getInstance(getApplication()).getTracker("token1", "user1");
        assertEquals(tracker.getToken(), "token1");
        assertEquals(tracker.getUser(), "user1");
    }

    final public void testCreateEvent() throws Exception {
        Event e = new Event()
                .setMetric("redbutton_click")
                .setValue(1)
                .addMetadata("SO:Name", "Android")
                .addMetadata("SO:Version", "5.1");

        assertEquals(e.getMetric(), "redbutton_click");
        assertEquals(e.getValue(), 1.0);

        HashMap<String, String> tags = e.addMetadata();
        assertEquals(tags.size(), 2);
        assertEquals(tags.get("SO:Name"), "Android");
        assertEquals(tags.get("SO:Version"), "5.1");
    }

    final public void testJsonifyEvent() throws Exception {
        Event e = new Event()
                .setMetric("redbutton_click")
                .setValue(1)
                .addMetadata("SO:Name", "Android")
                .addMetadata("SO:Version", "5.1");
        JSONObject json = e.toJSON();
        assertEquals(json.length(), 4);
        assertTrue(json.has("date"));
        assertEquals(json.get("metric"), "redbutton_click");
        assertEquals(json.get("value"), 1.0);

        assertEquals(json.getJSONObject("metadata").length(), 2);
        assertEquals(json.getJSONObject("metadata").get("SO:Name"), "Android");
        assertEquals(json.getJSONObject("metadata").get("SO:Version"), "5.1");
    }

    final public void testJsonifyEventFromTracker() throws Exception {
        Event e = new Event()
                .setMetric("redbutton_click")
                .setValue(1)
                .addMetadata("SO:Name", "Android")
                .addMetadata("SO:Version", "5.1");
        UTracker tracker = UReactMe.getInstance(getApplication()).getTracker("token1", "user1");
        JSONObject json = tracker.toJSON(e);
        assertEquals(json.length(), 4);
        assertTrue(json.has("date"));
        assertEquals(json.get("metric"), "redbutton_click");
        assertEquals(json.get("value"), 1.0);

        assertEquals(json.getJSONObject("metadata").length(), 3);
        assertEquals(json.getJSONObject("metadata").get("SO:Name"), "Android");
        assertEquals(json.getJSONObject("metadata").get("SO:Version"), "5.1");
        assertEquals(json.getJSONObject("metadata").get("userid"), "user1");
    }

    final public void testSendEvent() throws Exception {
        UReactMe uReactMe = UReactMe.getInstance(getApplication());
        UTracker tracker = uReactMe.getTracker("b5165f8b05c7d7df472d0065c849d0ddcfe74dd0", "user1");
        tracker.send(new Event()
                .setMetric("redbutton_click")
                .setValue(1)
                .addMetadata("SO:Name", "Android"));

    }
}