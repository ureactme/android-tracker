package me.ureact.traker;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pappacena on 13/12/15.
 */
public class UTracker {
    private String token;
    private Context context;
    private String user;

    protected UTracker(Context context, String token, String user) {
        this.context = context;
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return this.token;
    }

    public String getUser() {
        return this.user;
    }

    public void send(Event event) throws Exception {
        EventSender sender = new EventSender(this);
        sender.execute(event);
        sender.get();
    }

    public JSONObject toJSON(Event event) throws JSONException {
        JSONObject json = event.toJSON();
        json.getJSONObject("metadata").put("userid", this.getUser());
        return json;
    }
}
