package me.ureact.tracker;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pappacena on 13/12/15.
 */
public class UTracker {
    private String token;
    private Context context;
    private User user;

    protected UTracker(Context context, String token, String user) {
        this(context, token, new User(user));
    }

    protected UTracker(Context context, String token, User user) {
        this.context = context;
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return this.token;
    }

    public User getUser() {
        return this.user;
    }

    public void send(Event event) {
        try {
            EventSender sender = new EventSender(this);
            sender.execute(event);
            // sender.get();
        } catch(Exception e) {
            Log.e("ureact.me", "Error sending event: " + e.getMessage());
        }
    }

    public JSONObject toJSON(Event event) throws JSONException {
        JSONObject json = event.toJSON();

        // update event with the user's info
        this.getUser().toJSON(json.getJSONObject("metadata"));
        return json;
    }
}
