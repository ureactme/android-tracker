# UReact.ME Android tracker library

To tracke events on Android platform, you should add:

```
dependencies {
    // (...) your other dependencies

    compile 'me.ureact.tracker:tracker:0.0.3+'
}
```

to your build.gradle dependencies.

To initialize the library, you have to get a tracker using:

```
public class YourActivity extends Activity {
    private UTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = "YOUR API TOKEN";
        String appuser = "ANY ID THAT YOUR USER MIGHT HAVE ON YOUR APP (like his email, or an user_id code)";
        User user = User.getInstance(this)
                        .setId(appuser);
        this.tracker = UReactMe.getInstance(this).getTracker(token);
    }
}
```

Or you can add more information about your user:

```
public class YourActivity extends Activity {
    private UTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = "YOUR API TOKEN";
        User appuser = User.getInstance(this)
                            .setId("userid")
                            .setEmail("myuser@gmail.com")
                            .addPushNotificationId("push id")
                            .addName("John Doe")
                            .addPhoneNumber("+55 21 95555555");
        this.tracker = UReactMe.getInstance(this).getTracker(token);
    }
}
```

The information you set for the user will be persisted through your activities.
This way, you can set the user informations right after user registration,
or when he gives you the information you need, and it will be automatically
used by the tracker anywhere you send events.

Then, to track an event, you should do:

```
this.tracker.send(new Event().setMetric("redbutton_click"));
```

You can also add a value to this event:

```
this.tracker.send(new Event()
                    .setMetric("buy_item_click")
                    .setValue(5.35));
```

And extra metadata about the event:

```
this.tracker.send(new Event()
                    .setMetric("buy_item_click")
                    .setValue(5.35),
                    .addMetadata("moment", "before_loading_screen")
                    .addMetadata("recent_push", "yes"));

```


## How to release a new version?

- Make your changes
- Increase the `libraryVersion` on `tracker/build.gradle`
- Run `./gradlew install` to build stuff
- Run `./gradlew bintrayUpload` to send the new version

After that, don't forget to change the sample app:

- Change the `compile 'me.ureact.tracker:tracker:0.0.3+'` on sample/build.gradle
- Change the API calls acordingly
- Rebuild the example app
