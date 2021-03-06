# UReact.ME Android tracker library

To tracke events on Android platform, you should add:

```
dependencies {
    // (...) your other dependencies

    compile 'me.ureact.tracker:tracker:0.1.0+'
}
```

to your build.gradle dependencies. Add your UReact.me API key to your res/values/strings.xml

```xml
<resources>
    <string name="app_name">Tracker</string>
    <string name="ureactme_api_key">YOUR API TOKEN</string>
</resources>
```

To initialize the library, you have to get a tracker using:

```java
public class YourActivity extends Activity {
    private UTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String appuser = "ANY ID THAT YOUR USER MIGHT HAVE ON YOUR APP (like his email, or an user_id code)";
        User user = User.getInstance(this)
                        .setId(appuser);
        this.tracker = UReactMe.getInstance(this).getTracker();
    }
}
```

Or you can add more information about your user:

```java
public class YourActivity extends Activity {
    private UTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = "YOUR API TOKEN";
        User appuser = User.getInstance(this)
                            .setId("userid")
                            .setEmail("myuser@gmail.com")
                            .addGcmId("push id")
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

```java
this.tracker.send(new Event().setMetric("redbutton_click"));
```

You can also add a value to this event:

```java
this.tracker.send(new Event()
                    .setMetric("buy_item_click")
                    .setValue(5.35));
```

And extra metadata about the event:

```java
this.tracker.send(new Event()
                    .setMetric("buy_item_click")
                    .setValue(5.35),
                    .addMetadata("moment", "before_loading_screen")
                    .addMetadata("recent_push", "yes"));

```

By default, the events are sent to the backend every 30 minutes.
To send the events to the backend immediately, you can ask for the sync of an event
with a second argument to `this.tracker.send(event, forceSync)` method:

```java
this.tracker.send(new Event()
                    .setMetric("buy_item_click")
                    .setValue(5.35), true);
```

## API Reference

The ureact.me doesn't have a good API reference yet. So, here are the
endpoints used by this library:

### POST /api/v2/event
```json
{
    "date": "2015-12-07 19:34:00",
    "metric": "redbutton_click",
    "user": "user_id",
    "value": 1,
    "data": {
        "user-defined": 1
    }
}
```

The `user` key should be either the user's ID or the full user (see /api/v2/user endpoint below).
On "data", we can put whatever data we want.

### POST /api/v2/user
```json
{
    "id": "user identification",
    "auto_data": {"platform": {"type": "Android", "version": "4.4.1"}},
    "data": {"email": "foo@bla.com"}
}
```

`auto_data` key holds all the device-specific data, automatically collected.
`data` key holds user-defined data (you can put whatever you want here)


## How to release a new version?

- Make your changes
- Increase the `libraryVersion` on `tracker/build.gradle`
- Run `./gradlew install` to build stuff
- Run `./gradlew bintrayUpload` to send the new version

After that, don't forget to change the sample app:

- Change the `compile 'me.ureact.tracker:tracker:0.0.3+'` on sample/build.gradle
- Change the API calls acordingly
- Rebuild the example app
