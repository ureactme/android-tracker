package me.ureact.tracker;

import android.content.Context;
import android.text.TextUtils;

import me.ureact.tracker.exceptions.EmptyTokenException;
import me.ureact.tracker.util.ULogger;

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
            return "https://app.ureact.me";
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
        try {
            String token = this.context.getString(R.string.ureactme_api_key);
            if (TextUtils.isEmpty(token)) {
                String msg = "You must provide a ureactme_api_key on your res/values/strings.xml file";
                throw new EmptyTokenException(msg);
            }
            return UTracker.getInstance(this.context, token);
        } catch (EmptyTokenException e) {
            ULogger.e(e.getMessage());
            return null;
        }
    }
}
