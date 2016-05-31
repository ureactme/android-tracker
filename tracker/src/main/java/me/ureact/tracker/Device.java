package me.ureact.tracker;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by pappacena on 27/12/15.
 */
public class Device {
    public static final String VERSION_RELEASE = Build.VERSION.RELEASE;
    public static final int VERSION_SDK = Build.VERSION.SDK_INT;
    public static final String BRAND = Build.BRAND;
    public static final String MANUFACTURER = Build.MANUFACTURER;
    public static final String MODEL = Build.MODEL;
    public static final String SERIAL = Build.SERIAL;

    public static JSONObject toJSON(JSONObject json) throws JSONException {
        json.put("platform:type", "Android");
        json.put("platform:release", VERSION_RELEASE);
        json.put("platform:sdk", VERSION_SDK);
        json.put("platform:brand", BRAND);
        json.put("platform:manufacturer", MANUFACTURER);
        json.put("platform:model", MODEL);
        json.put("platform:serial", SERIAL);
        json.put("platform:root", isRooted());
        return json;
    }

    public static JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        return Device.toJSON(json);
    }

    private static boolean isRooted() {
        String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
        for (String where : places) {
            if (new File(where + "su").exists()) {
                return true;
            }
        }
        return false;
    }
}
