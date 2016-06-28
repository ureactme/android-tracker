package me.ureact.tracker;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

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
    private final Context context;

    private Device() {
        context = null;
    }

    private Device(Context context) {
        this.context = context;
    }

    public static Device getInstance(Context context) {
        return new Device(context);
    }

    public JSONObject toJSON(JSONObject json) throws JSONException {
        json.put("platform:type", "Android");
        json.put("platform:release", VERSION_RELEASE);
        json.put("platform:sdk", VERSION_SDK);
        json.put("platform:brand", BRAND);
        json.put("platform:manufacturer", MANUFACTURER);
        json.put("platform:model", MODEL);
        json.put("platform:serial", SERIAL);
        json.put("platform:root", isRooted());

        String packageName = getPackage();
        if (packageName != null) {
            json.put("platform:package", packageName);
        }

        int version = getVersion();
        if (version != 0) {
            json.put("platform:version", version);
        }

        String imei = getIMEI();
        if (imei != null) {
            json.put("platform:imei", imei);
        }

        String androidId = getAndroidId();
        if (androidId != null) {
            json.put("platform:android_id", androidId);
        }
        return json;
    }

    public String getAndroidId() {
        String id;
        try {
            id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (NullPointerException e) {
            return null;
        }
        return id;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        return toJSON(json);
    }

    private String getIMEI() {
        String deviceId;
        try {
            TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = mngr.getDeviceId();
        } catch (NullPointerException e) {
            return null;
        }
        return deviceId;
    }

    /**
     * Package name of the app
     */
    private String getPackage() {
        try {
            return context.getApplicationContext().getPackageName();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The version name of this package from the manifest
     */
    private int getVersion() {
        try {
            PackageInfo pInfo = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Check if the device was rooted
     */
    private boolean isRooted() {
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
