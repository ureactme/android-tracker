package me.ureact.tracker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * Created by pappacena on 31/12/15.
 */
public class UserTest extends ApplicationTestCase<Application> {
    public UserTest() {
        super(Application.class);
    }

    public final void setUp() {
        User.clear(getContext());
    }

    public final void tearDown() {
        User.clear(getContext());
    }

    final public void testGetSharedPref() throws Exception {
        SharedPreferences got = User.getInstance(getContext()).getSharedPref();

        SharedPreferences expected = getContext().getSharedPreferences(
                "me.ureact.tracker.test:ureactme:tracker:user", Context.MODE_PRIVATE);

        assertEquals(got, expected);
    }

    final public void testSetUserDataPersists() throws Exception {
        User user = User.getInstance(getContext());

        user.setId("123");
        user.setName("Thiago F. Pappacena");
        user.setGcmId("gcmid123");
        user.setPhoneNumber("+55 21 95555555");
        user.setEmail("test@ureact.me");

        User.instance = null;
        user = User.getInstance(getContext());
        assertEquals(user.getId(), "123");
        assertEquals(user.getName(), "Thiago F. Pappacena");
        assertEquals(user.getGcmId(), "gcmid123");
        assertEquals(user.getPhoneNumber(), "+55 21 95555555");
        assertEquals(user.getEmail(), "test@ureact.me");
    }
}
