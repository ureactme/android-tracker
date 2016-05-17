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

    protected UTracker(Context context, String token) {
        this.context = context;
        this.token = token;
        this.user = User.getInstance(context);
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
        User user = this.getUser();
        if (!user.isSync()) {
            json.put("user", user.toJSON());
        }
        else {
            json.put("user", user.getId());
        }
        return json;
    }
}
