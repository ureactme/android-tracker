package me.ureact.tracker.example;

import android.app.Application;
import android.test.ApplicationTestCase;

import me.ureact.tracker.Event;
import me.ureact.tracker.UReactMe;
import me.ureact.tracker.User;

/**
 * Created by Valeriy Palamarchuk on 6/1/16
 */
public class DryRunTest extends ApplicationTestCase<Application> {
    public DryRunTest() {
        super(Application.class);
    }

    @Override
    public final void setUp() throws Exception {
        super.setUp();
        createApplication();
        User.clear(getContext());
    }

    public final void tearDown() {
        User.clear(getContext());
    }

    final public void testDryRun() throws Exception {
        // False by default
        UReactMe.getInstance(getContext())
                .getTracker()
                .send(new Event());
        assertEquals(UReactMe.getInstance(getContext()).getTracker().isDryRun(), false);

        // True
        UReactMe.getInstance(getContext())
                .getTracker()
                .setDryRun(true)
                .send(new Event());
        assertEquals(UReactMe.getInstance(getContext()).getTracker().isDryRun(), true);

        // False
        UReactMe.getInstance(getContext())
                .getTracker()
                .setDryRun(false)
                .send(new Event());
        assertEquals(UReactMe.getInstance(getContext()).getTracker().isDryRun(), false);
    }
}
