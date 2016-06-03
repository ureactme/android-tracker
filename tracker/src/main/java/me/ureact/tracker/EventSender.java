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
    private final boolean dryRun;
    private final UTracker tracker;

    public EventSender(UTracker tracker, boolean dryRun) {
        this.tracker = tracker;
        this.dryRun = dryRun;
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
        String responseText = "";

        try {
            JSONObject json = this.tracker.toJSON(event);
            String payload = json.toString();
            if (dryRun) {
                Log.d("ureact.me", "DRY RUN -> " + payload);
            } else {
                String baseUrl = UReactMe.getBaseUrl(this.tracker.getContext());
                URL url = new URL(baseUrl + "/api/v2/event/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setAllowUserInteraction(false);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept-Charset", "utf-8");
                connection.setRequestProperty("Authorization", "Token " + this.tracker.getToken());
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("Content-Length", "" + payload.getBytes("UTF8").length);
                connection.setFixedLengthStreamingMode(payload.getBytes().length);

                connection.connect();
                DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
                printout.write(payload.getBytes("UTF-8"));
                printout.flush();
                printout.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    responseText += line;
                }
                br.close();

                Log.d("ureact.me", connection.getResponseCode() + " " + baseUrl + " -> " + payload);

                if (connection.getResponseCode() > 299 || connection.getResponseCode() < 200) {
                    Log.e("ureact.me", "Connection error: HTTP " + connection.getResponseCode());
                    Log.e("ureact.me", "Response: " + responseText);
                }

                if (!this.tracker.getUser().isSync()) {
                    this.tracker.getUser().markAsSync();
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("ureact.me", "Connection problem: " + e.getMessage());
            Log.e("ureact.me", "Response: " + responseText);
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
