package us.utrak.tracker;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BasicUsageTest extends ApplicationTestCase<Application> {
    public BasicUsageTest() {
        super(Application.class);
    }

    final public void testInitializeUTrakUs() throws Exception {
        UTracker tracker = UTrakUs.getInstance(getApplication()).getTracker("token1", "user1");
        assertEquals(tracker.getToken(), "token1");
        assertEquals(tracker.getUser(), "user1");
    }
}