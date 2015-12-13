package us.utrak.tracker;

import android.content.Context;

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

    public void send(Event event) {

    }
}
