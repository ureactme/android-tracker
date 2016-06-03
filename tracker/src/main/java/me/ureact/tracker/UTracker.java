package me.ureact.tracker;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

import me.ureact.tracker.db.AddEventCallback;
import me.ureact.tracker.db.DBHelper;
import me.ureact.tracker.db.GetEventsCallback;

/**
 * Created by pappacena on 13/12/15.
 */
public class UTracker {
    private static final long SYNC_PERIOD = 1000 * 60 * 30;
    private static UTracker instance;
    private DBHelper db;
    private String token;
    private Context context;
    private UReactUser user;
    private boolean dryRun;

    private UTracker() {
    }

    private UTracker(Context context, String token) {
        this.context = context;
        this.token = token;
        this.db = new DBHelper(context);
        this.user = UReactUser.getInstance(context);
    }

    protected static synchronized UTracker getInstance(Context context, String token) {
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

    public UReactUser getUser() {
        return this.user;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    /**
     * uReact does not send info to the backend but still show it in logcat if the
     * {@link UTracker#dryRun} is <code>true</code>
     */
    public UTracker setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
        return this;
    }

    public boolean isTime2Sync() {
        Date lastDateSync = getUser().getLastDateSync();
        Date now = new Date();
        long min = (now.getTime() - lastDateSync.getTime()) / SYNC_PERIOD;
        return min > 1.0;
    }

    public void syncSuccessful() {
        getUser().setLastDateSync();
        db.deleteAllEventsInBackground();
    }

    /**
     * Store the event to DB and sync if needed
     *
     * @param event to store
     */
    public void send(final Event event) {
        db.addEventInBackground(event, new AddEventCallback() {
            @Override
            public void onFinish() {
                if (isBackendAvailable()) {
                    sync();
                }
            }
        });
    }

    /**
     * Check if it's time to sync: true -> process sync, false -> do nothing
     */
    private void sync() {
        if (isTime2Sync()) {
            db.getEventsInBackground(new GetEventsCallback() {
                @SuppressWarnings("unchecked")
                @Override
                public void onFinish(ArrayList<Event> events) {
                    new EventSender(UTracker.this, dryRun).execute(events);
                }
            });
        }
    }

    public JSONObject toJSON(ArrayList<Event> events) {
        JSONArray jsonEvents = new JSONArray();
        JSONObject json = new JSONObject();
        try {
            for (Event e : events) {
                jsonEvents.put(e.toJSON());
            }
            json.put("events", jsonEvents);

            // update event with the user's info
            UReactUser user = this.getUser();
            if (!user.isSync()) {
                json.put("user", user.toJSON());
            } else {
                json.put("user", user.getId());
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
            Log.e("ureact.me", "Failed to parse ArrayList<Event> to json: " + events.toString());
        }
        return json;
    }

    public JSONObject toJSON(Event event) throws JSONException {
        JSONObject json = event.toJSON();

        // update event with the user's info
        UReactUser user = this.getUser();
        if (!user.isSync()) {
            json.put("user", user.toJSON());
        } else {
            json.put("user", user.getId());
        }
        return json;
    }

    /**
     * Check if the backend available to call
     */
    public boolean isBackendAvailable() {
        try {
            String url = UReactMe.getBaseUrl(context).replaceFirst("http://", "");
            InetAddress ip = InetAddress.getByName(url);
            return !ip.equals("");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
