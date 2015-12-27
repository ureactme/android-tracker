package me.ureact.tracker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pappacena on 27/12/15.
 */
public class User {
    private String id;
    private String email;
    private String pushNotificationId;
    private String phoneNumber;
    private String name;

    public User(String id) {
        this.id = id;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setPushNotificationId(String pushNotificationId) {
        this.pushNotificationId = pushNotificationId;
        return this;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPushNotificationId() {
        return this.pushNotificationId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        return this.toJSON(json);
    }

    public JSONObject toJSON(JSONObject json) throws JSONException {
        json.put("user:id", this.id);

        if (this.name != null && this.name.length() > 0) {
            json.put("user:name", this.name);
        }

        if (this.email != null && this.email.length() > 0) {
            json.put("user:email", this.email);
        }

        if (this.pushNotificationId != null && this.pushNotificationId.length() > 0) {
            json.put("user:pushNotificationId", this.pushNotificationId);
        }

        if (this.phoneNumber != null && this.phoneNumber.length() > 0) {
            json.put("user:phoneNumber", this.phoneNumber);
        }

        return json;
    }
}
