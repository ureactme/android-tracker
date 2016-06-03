package me.ureact.tracker;

import android.content.Context;
import android.util.Log;

/**
 * Created by pappacena on 12/12/15.
 */
public class UReactMe {
    private static UReactMe instance;
    private Context context;

    private UReactMe() {

    }

    private UReactMe(Context context) {
        this.context = context;
    }

    public static String getBaseUrl(Context context) {
        try {
            return context.getString(R.string.ureactme_url);
        } catch (Exception e) {
            return "http://app.ureact.me";
        }
    }

    public static synchronized UReactMe getInstance(Context context) {
        if (instance == null) {
            instance = new UReactMe(context);
        }
        return instance;
    }

    public UReactUser getUser() {
        return getTracker().getUser();
    }

    public UTracker getTracker(String token) {
        return UTracker.getInstance(this.context, token);
    }

    public UTracker getTracker() {
        String token = this.context.getString(R.string.ureactme_api_key);
        if (token == null || token.length() == 0) {
            String msg = "You must provide a ureactme_api_key on your res/values/strings.xml file";
            Log.e("ureact.me", msg);
            return null;
        }
        return UTracker.getInstance(this.context, token);
    }
}
