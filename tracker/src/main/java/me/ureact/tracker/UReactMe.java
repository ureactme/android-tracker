package me.ureact.tracker;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import me.ureact.tracker.exceptions.EmptyTokenException;

/**
 * Created by pappacena on 12/12/15.
 */
public class UReactMe {
    private Context context;
    private static HashMap<Context, UReactMe> instances = new HashMap<Context, UReactMe>();

    private UReactMe(Context context) {
        this.context = context;
    }

    public UTracker getTracker(String token) {
        return new UTracker(this.context, token);
    }

    public UTracker getTracker() {
        String token = this.context.getString(R.string.ureactme_api_key);
        if(token == null || token.length() == 0) {
            String msg = "You must provide a ureactme_api_key on your res/values/strings.xml file";
            Log.e("ureact.me", msg);
            return null;
        }
        return new UTracker(this.context, token);
    }

    public static String getBaseUrl(Context context) {
        try {
            return context.getString(R.string.ureactme_url);
        } catch(Exception e) {
            return "http://app.ureact.me";
        }
    }

    public static UReactMe getInstance(Context context) {
        if (!UReactMe.instances.containsKey(context)) {
            UReactMe b = new UReactMe(context);
            UReactMe.instances.put(context, b);
        }
        return UReactMe.instances.get(context);
    }
}
