package me.ureact.tracker;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import me.ureact.tracker.util.ULogger;

/**
 * Created by pappacena on 15/12/15.
 */
public class EventSender extends AsyncTask<ArrayList<Event>, Event, Void> {
    private final boolean dryRun;
    private final UTracker tracker;

    public EventSender(UTracker tracker, boolean dryRun) {
        this.tracker = tracker;
        this.dryRun = dryRun;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(ArrayList<Event>... params) {
        ArrayList<Event> events = params[0];
        String payload = tracker.toJSON(events).toString();
        if (dryRun) {
            ULogger.d("DRY RUN -> " + payload);
            tracker.syncSuccessful();
        } else {
            sendEvent(payload);
        }
        return null;
    }

    private void sendEvent(String payload) {
        HttpURLConnection connection = null;
        String responseText = "";
        try {
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

            InputStream in;
            if (connection.getResponseCode() >= 400) {
                in = connection.getErrorStream();
            } else {
                in = connection.getInputStream();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line;
            while ((line = br.readLine()) != null) {
                responseText += line;
            }
            br.close();

            if (connection.getResponseCode() > 299 || connection.getResponseCode() < 200) {
                ULogger.e("Connection error: HTTP " + connection.getResponseCode());
                ULogger.e("Response: " + responseText);
            }

            tracker.syncSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            ULogger.e("Connection problem: " + e.getMessage());
            ULogger.e("Response: " + responseText);
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
