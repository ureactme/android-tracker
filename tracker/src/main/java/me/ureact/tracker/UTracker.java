package me.ureact.tracker;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pappacena on 13/12/15.
 */
public class UTracker {
    private static UTracker instance;
    private String token;
    private Context context;
    private User user;
    private boolean dryRun;

    private UTracker() {

    }

    private UTracker(Context context, String token) {
        this.context = context;
        this.token = token;
        this.user = User.getInstance(context);
    }

    public static synchronized UTracker getInstance(Context context, String token) {
        if (instance == null) {
            instance = new UTracker(context, token);
        }
        return instance;
    }

    public Context getContext() {
        return this.context;
    }

    public String getToken() {
        return this.token;
    }

    public User getUser() {
        return this.user;
    }

    /**
     * uReact does not send info to the backend but still show it in logcat if the
     * {@link UTracker#dryRun} is <code>true</code>
     */
    public UTracker setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
        return this;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void send(Event event) {
        try {
            EventSender sender = new EventSender(this, dryRun);
            sender.execute(event);
        } catch(Exception e) {
            Log.e("ureact.me", "Error sending event: " + e.getMessage());
        }
    }

    public JSONObject toJSON(Event event) throws JSONException {
        JSONObject json = event.toJSON();

        // update event with the user's info
        User user = this.getUser();
        if (!user.isSync()) {
            json.put("user", user.toJSON());
        } else {
            json.put("user", user.getId());
        }
        return json;
    }
}
