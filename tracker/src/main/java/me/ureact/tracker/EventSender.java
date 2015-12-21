package me.ureact.tracker;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pappacena on 15/12/15.
 */
public class EventSender extends AsyncTask<Event, Event, Void> {
    private UTracker tracker;

    public EventSender(UTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    protected Void doInBackground(Event... params) {
        for (Event e : params) {
            this.sendEvent(e);
            this.publishProgress(e);
        }
        return null;
    }

    private void sendEvent(Event event) {
        HttpURLConnection connection = null;

        try {
            JSONObject json = this.tracker.toJSON(event);
            String payload = json.toString();

            URL url = new URL(UReactMe.BASE_URL + "/api/v1/metric/");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setAllowUserInteraction(false);
            connection.setUseCaches(false);
            // connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
            // connection.setReadTimeout(TIMEOUT_READ_MILLIS);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Authorization", "Token " + this.tracker.getToken());
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Content-Length", "" + payload.toString().getBytes("UTF8").length);
            connection.setFixedLengthStreamingMode(payload.getBytes().length);

            connection.connect();
            Log.d("ureact.me", "Sending " + payload);
            DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
            printout.write(payload.toString().getBytes("UTF-8"));
            printout.flush();
            printout.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String responseText = "", line = "";
            while ((line = br.readLine()) != null) {
                responseText += line;
            }
            br.close();

            Log.d("ureact.me", "Got response code " + connection.getResponseCode() + " - " + responseText);
        } catch (IOException | JSONException e) {
            Log.e("ureact.me", "Connection problem: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Void result) {
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Event... events) {
    }
}
