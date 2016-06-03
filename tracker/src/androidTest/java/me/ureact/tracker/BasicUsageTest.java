package me.ureact.tracker;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BasicUsageTest extends ApplicationTestCase<Application> {
    public BasicUsageTest() {
        super(Application.class);
    }

    public final void setUp() {
        UReactUser.clear(getContext());
    }

    public final void tearDown() {
        UReactUser.clear(getContext());
    }

    final public void testInitializeUTracker() throws Exception {
        UReactUser.getInstance(getContext()).setId("user1");
        UTracker tracker = UReactMe.getInstance(getApplication()).getTracker("token1");
        assertEquals(tracker.getToken(), "token1");
        assertEquals(tracker.getUser().getId(), "user1");
    }

    final public void testInitializeUTrackerWithUserObject() throws Exception {
        UReactUser u = UReactUser.getInstance(getContext())
                .setId("userid1")
                .setEmail("bla@foo.com")
                .setGcmId("devid");
        UTracker tracker = UReactMe.getInstance(getApplication()).getTracker("token1");

        assertEquals(tracker.getUser(), u);
        assertEquals(tracker.getToken(), "token1");
    }

    final public void testCreateEvent() throws Exception {
        Event e = new Event()
                .setValue(1)
                .addMetadata("SO:Name", "Android")
                .addMetadata("SO:Version", "5.1");

        assertEquals(e.getValue(), 1.0);

        HashMap<String, String> tags = e.getMetadata();
        assertEquals(tags.size(), 2);
        assertEquals(tags.get("SO:Name"), "Android");
        assertEquals(tags.get("SO:Version"), "5.1");
    }

    final public void testJsonifyEvent() throws Exception {
        Event e = new Event()
                .setValue(1)
                .addMetadata("SO:Name", "Android")
                .addMetadata("SO:Version", "5.1");
        JSONObject json = e.toJSON();
        assertEquals(json.length(), 4);
        assertTrue(json.has("date"));
        assertEquals(json.get("metric"), "redbutton_click");
        assertEquals(json.get("value"), 1.0);

        assertEquals(json.getJSONObject("metadata").length(), 4);
        assertEquals(json.getJSONObject("metadata").get("platform:type"), "Android");
        assertEquals(json.getJSONObject("metadata").get("platform:release"), Device.VERSION_RELEASE);
        assertEquals(json.getJSONObject("metadata").get("SO:Name"), "Android");
        assertEquals(json.getJSONObject("metadata").get("SO:Version"), "5.1");
    }

    final public void testJsonifyEventFromTracker() throws Exception {
        UReactUser.getInstance(getContext())
                .setId("user1");
        Event e = new Event()
                .setValue(1)
                .addMetadata("SO:Name", "Android")
                .addMetadata("SO:Version", "5.1");
        UTracker tracker = UReactMe.getInstance(getApplication()).getTracker("token1");
        JSONObject json = tracker.toJSON(e);
        assertEquals(json.length(), 4);
        assertTrue(json.has("date"));
        assertEquals(json.get("metric"), "redbutton_click");
        assertEquals(json.get("value"), 1.0);

        Log.e("tag", json.getJSONObject("metadata").toString(2));
        assertEquals(5, json.getJSONObject("metadata").length());
        assertEquals(json.getJSONObject("metadata").get("platform:type"), "Android");
        assertEquals(json.getJSONObject("metadata").get("SO:Name"), "Android");
        assertEquals(json.getJSONObject("metadata").get("SO:Version"), "5.1");
        assertEquals(json.getJSONObject("metadata").get("user:id"), "user1");
    }

    final public void testSendEvent() throws Exception {
        UReactMe uReactMe = UReactMe.getInstance(getApplication());
        UReactUser.getInstance(getContext()).setId("user1");
        UTracker tracker = uReactMe.getTracker("b5165f8b05c7d7df472d0065c849d0ddcfe74dd0");
        tracker.send(new Event()
                .setValue(1)
                .addMetadata("SO:Name", "Android"));
    }
}