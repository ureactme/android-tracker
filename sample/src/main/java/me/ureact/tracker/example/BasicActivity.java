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

        UReactMe.getInstance(this).getTracker().setDryRun(true).setSyncPeriod(10000)
                .getUser()
                .setId("my_app_user_id")
                .setEmail("exampleuser@users.com")
                .setGcmId("push-id")
                .setName("John Doe");
        this.tracker = UReactMe.getInstance(this).getTracker();
    }

    public void onRedButtonClick(View v) {
        // Create the event and try to send it immediatly (dont wait sync period)
        // this.tracker.send(event, true)
        this.tracker.send(new Event()
                .setCategory("redbutton")
                .setAction("click"), true);
    }

    public void onGreenButtonClick(View v) {
        this.tracker.send(new Event()
                .setCategory("greenbutton")
                .setAction("click"));
    }

    public void onBlueButtonClick(View v) {
        this.tracker.send(new Event()
                .setCategory("bluebutton")
                .setAction("click"));
    }
}
