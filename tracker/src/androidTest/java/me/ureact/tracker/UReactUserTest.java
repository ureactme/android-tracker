package me.ureact.tracker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ApplicationTestCase;

/**
 * Created by pappacena on 31/12/15.
 */
public class UReactUserTest extends ApplicationTestCase<Application> {
    public UReactUserTest() {
        super(Application.class);
    }

    public final void setUp() {
        UReactUser.clear(getContext());
    }

    public final void tearDown() {
        UReactUser.clear(getContext());
    }

    final public void testGetSharedPref() throws Exception {
        SharedPreferences got = UReactUser.getInstance(getContext()).getSharedPref();

        SharedPreferences expected = getContext().getSharedPreferences(
                "me.ureact.tracker.test:ureactme:tracker:user", Context.MODE_PRIVATE);

        assertEquals(got, expected);
    }

    final public void testSetUserDataPersists() throws Exception {
        UReactUser user = UReactUser.getInstance(getContext());

        user.setId("123");
        user.setName("Thiago F. Pappacena");
        user.setGcmId("gcmid123");
        user.setPhoneNumber("+55 21 95555555");
        user.setEmail("test@ureact.me");

        UReactUser.instance = null;
        user = UReactUser.getInstance(getContext());
        assertEquals(user.getId(), "123");
        assertEquals(user.getName(), "Thiago F. Pappacena");
        assertEquals(user.getGcmId(), "gcmid123");
        assertEquals(user.getPhoneNumber(), "+55 21 95555555");
        assertEquals(user.getEmail(), "test@ureact.me");
    }
}
