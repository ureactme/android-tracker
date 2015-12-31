package me.ureact.tracker;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by pappacena on 12/12/15.
 */
public class UReactMe {
    public final static String BASE_URL = "http://ureact.me";
    private Context context;
    private static HashMap<Context, UReactMe> instances = new HashMap<Context, UReactMe>();

    private UReactMe(Context context) {
        this.context = context;
    }

    public UTracker getTracker(String token) {
        return new UTracker(this.context, token);
    }

    public static UReactMe getInstance(Context context) {
        if (!UReactMe.instances.containsKey(context)) {
            UReactMe b = new UReactMe(context);
            UReactMe.instances.put(context, b);
        }
        return UReactMe.instances.get(context);
    }
}
