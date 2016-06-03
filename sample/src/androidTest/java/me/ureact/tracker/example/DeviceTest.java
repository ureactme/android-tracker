package me.ureact.tracker.example;

import android.app.Application;
import android.os.Build;
import android.test.ApplicationTestCase;

import org.json.JSONObject;

import me.ureact.tracker.Device;
import me.ureact.tracker.UReactUser;

/**
 * Check if the lib returns correct device's info
 */
public class DeviceTest extends ApplicationTestCase<Application> {
    public static final String VERSION_RELEASE = Build.VERSION.RELEASE;
    public static final int VERSION_SDK = Build.VERSION.SDK_INT;
    public static final String BRAND = Build.BRAND;
    public static final String MANUFACTURER = Build.MANUFACTURER;
    public static final String MODEL = Build.MODEL;
    public static final String SERIAL = Build.SERIAL;

    public DeviceTest() {
        super(Application.class);
    }

    public DeviceTest(Class<Application> applicationClass) {
        super(applicationClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        UReactUser.clear(getContext());
    }

    @Override
    public final void tearDown() {
        UReactUser.clear(getContext());
    }

    final public void testDeviceInfo() throws Exception {
        JSONObject json = Device.getInstance(getContext()).toJSON();

        assertEquals(json.getString("platform:type"), "Android");
        assertEquals(json.getString("platform:release"), VERSION_RELEASE);
        assertEquals(json.getInt("platform:sdk"), VERSION_SDK);
        assertEquals(json.getString("platform:brand"), BRAND);
        assertEquals(json.getString("platform:manufacturer"), MANUFACTURER);
        assertEquals(json.getString("platform:model"), MODEL);
        assertEquals(json.getString("platform:serial"), SERIAL);
        assertEquals(json.getBoolean("platform:root"), false);
        assertEquals(json.getString("platform:package"), getContext().getPackageName());
        assertEquals(json.getInt("platform:version"), getContext().getPackageManager()
                .getPackageInfo(getContext().getPackageName(), 0).versionCode);
    }
}
