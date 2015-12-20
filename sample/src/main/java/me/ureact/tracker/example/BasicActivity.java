package me.ureact.tracker.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import me.ureact.tracker.Event;
import me.ureact.tracker.UReactMe;
import me.ureact.tracker.UTracker;

/**
 * Created by pappacena on 16/12/15.
 */
public class BasicActivity extends Activity {
    private UTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_activity);

        String token = "b5165f8b05c7d7df472d0065c849d0ddcfe74dd0";
        String appuser = "my_app_user_id";
        this.tracker = UReactMe.getInstance(this).getTracker(token, appuser);
    }

    public void onRedButtonClick(View v) {
        Log.d("example", "tracker: " + this.tracker);
        this.tracker.send(new Event().setMetric("redbutton_click"));
    }

    public void onGreenButtonClick(View v) {
        this.tracker.send(new Event().setMetric("greenbutton_click"));
    }

    public void onBlueButtonClick(View v) {
        this.tracker.send(new Event().setMetric("bluebutton_click"));
    }
}
