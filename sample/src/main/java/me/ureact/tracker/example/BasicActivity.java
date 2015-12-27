package me.ureact.tracker.example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import me.ureact.tracker.Event;
import me.ureact.tracker.UReactMe;
import me.ureact.tracker.UTracker;
import me.ureact.tracker.User;

/**
 * Created by pappacena on 16/12/15.
 */
public class BasicActivity extends Activity {
    private UTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_activity);

        String token = "3d2c3f5fa903c250341a501bb1d1ced9153c0cb5";
        User appuser = new User("my_app_user_id")
                .setEmail("exampleuser@users.com")
                .setPushNotificationId("push-id")
                .setName("John Doe");
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
