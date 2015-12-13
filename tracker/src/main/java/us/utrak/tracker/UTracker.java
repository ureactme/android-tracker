package us.utrak.tracker;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        try {
            JSONObject json = this.toJSON(event);

            URL url = new URL(UTrakUs.BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setAllowUserInteraction(false);
            connection.setUseCaches(false);
            // connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
            // connection.setReadTimeout(TIMEOUT_READ_MILLIS);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Length", ""+json.toString().getBytes("UTF8").length);

            OutputStream os = connection.getOutputStream();
            os.write(json.toString().getBytes());
            os.close();
        } catch(IOException|JSONException e) {
            Log.e("utrak.us", "Connection problem: " + e.getMessage());
        }
    }

    public JSONObject toJSON(Event event) throws JSONException {
        JSONObject json = event.toJSON();
        json.getJSONObject("metadata").put("user", this.getUser());
        return json;
    }
}
