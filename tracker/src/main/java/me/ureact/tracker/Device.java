package me.ureact.tracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pappacena on 27/12/15.
 */
public class Device {

    public String getOSRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    public JSONObject toJSON(JSONObject json) throws JSONException {
        json.put("platform:type", "Android");
        json.put("platform:release", this.getOSRelease());
        return json;
    }
}
