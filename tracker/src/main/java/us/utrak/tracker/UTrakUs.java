package us.utrak.tracker;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by pappacena on 12/12/15.
 */
public class UTrakUs {
    public final static String BASE_URL = "http://192.168.1.5:8080";
    private Context context;
    private static HashMap<Context, UTrakUs> instances = new HashMap<Context, UTrakUs>();

    private UTrakUs(Context context) {
        this.context = context;
    }

    public UTracker getTracker(String token, String user) {
        return new UTracker(this.context, token, user);
    }

    public static UTrakUs getInstance(Context context) {
        if (!UTrakUs.instances.containsKey(context)) {
            UTrakUs b = new UTrakUs(context);
            UTrakUs.instances.put(context, b);
        }
        return UTrakUs.instances.get(context);
    }
}
