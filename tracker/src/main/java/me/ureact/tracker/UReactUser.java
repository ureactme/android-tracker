package me.ureact.tracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by pappacena on 27/12/15.
 */
public class UReactUser {
    private static final String PREF_ID_KEY = "user:id";
    private static final String PREF_EMAIL_KEY = "user:email";
    private static final String PREF_GCM_ID_KEY = "user:gcm_id";
    private static final String PREF_PHONE_NUMBER_KEY = "user:phone_number";
    private static final String PREF_NAME_KEY = "user:name";
    private static final String PREF_IS_SYNC_KEY = "user:is_sync";
    private static final String PREF_LAST_DATE_SYNC = "user:last_date_sync";

    protected static UReactUser instance;

    private Context context;
    private String id;
    private Date lastDateSync;
    private String email;
    private String gcmId;
    private String phoneNumber;
    private String name;
    private boolean isSync = false;
    private SharedPreferences pref;

    private UReactUser(Context context) {
        this.context = context;
    }

    private UReactUser() {

    }

    /**
     * Returns the instance of the user to be tracked by UReact.me
     *
     * In the registration process, you need to get this User instance,
     * and set the user's information (like id, name, email, etc) using the setters of the
     * returning object.
     *
     * @param context
     * @return
     */
    protected static UReactUser getInstance(Context context) {
        if (UReactUser.instance == null) {
            UReactUser.instance = new UReactUser(context);
            UReactUser.instance.loadFromSharedPref();
        }
        return UReactUser.instance;
    }

    /**
     * Clear all user data from shared preferences and singleton instance
     *
     * @param context
     */
    public static void clear(Context context) {
        UReactUser u = UReactUser.getInstance(context);
        SharedPreferences.Editor e = u.getSharedPref().edit();
        e.clear();
        e.commit();
        u.loadFromSharedPref();
    }

    protected SharedPreferences getSharedPref() {
        if (this.pref == null) {
            String pkgName = context.getPackageName();
            this.pref = this.context.getSharedPreferences(
                    pkgName + ":ureactme:tracker:user", Context.MODE_PRIVATE);
        }
        return this.pref;
    }

    /**
     * Loads all data of the user from the shared pref file
     */
    private void loadFromSharedPref() {
        SharedPreferences pref = this.getSharedPref();
        this.id = pref.getString(PREF_ID_KEY, null);
        this.email = pref.getString(PREF_EMAIL_KEY, null);
        this.gcmId = pref.getString(PREF_GCM_ID_KEY, null);
        this.phoneNumber = pref.getString(PREF_PHONE_NUMBER_KEY, null);
        this.name = pref.getString(PREF_NAME_KEY, null);
        this.isSync = pref.getBoolean(PREF_IS_SYNC_KEY, false);
        this.lastDateSync = new Date(pref.getLong(PREF_LAST_DATE_SYNC, 0));
    }

    protected void setSharedPrefValue(String key, String value) {
        SharedPreferences.Editor edit = this.getSharedPref().edit();
        edit.putString(key, value);
        edit.putBoolean(PREF_IS_SYNC_KEY, false);
        edit.commit();
    }

    protected void setSharedPrefValue(String key, long value) {
        SharedPreferences.Editor edit = this.getSharedPref().edit();
        edit.putLong(key, value);
        edit.putBoolean(PREF_IS_SYNC_KEY, false);
        edit.commit();
    }

    protected void setSharedPrefValue(String key, boolean value) {
        SharedPreferences.Editor edit = this.getSharedPref().edit();
        edit.putBoolean(key, value);
        edit.putBoolean(PREF_IS_SYNC_KEY, false);
        edit.commit();
    }

    public UReactUser setLastDateSync() {
        this.lastDateSync = new Date();
        this.setSharedPrefValue(PREF_LAST_DATE_SYNC, lastDateSync.getTime());
        return this;
    }

    public void markAsSync() {
        this.isSync = true;
        this.setSharedPrefValue(PREF_IS_SYNC_KEY, true);
    }

    public boolean isSync() {
        return this.isSync;
    }

    public String getName() {
        return this.name;
    }

    public UReactUser setName(String name) {
        this.name = name;
        this.setSharedPrefValue(PREF_NAME_KEY, name);
        return this;
    }

    public Date getLastDateSync() {
        return this.lastDateSync;
    }

    public String getId() {
        return this.id;
    }

    public UReactUser setId(String id) {
        this.id = id;
        this.setSharedPrefValue(PREF_ID_KEY, id);
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public UReactUser setEmail(String email) {
        this.email = email;
        this.setSharedPrefValue(PREF_EMAIL_KEY, email);
        return this;
    }

    public String getGcmId() {
        return this.gcmId;
    }

    public UReactUser setGcmId(String gcmId) {
        this.gcmId = gcmId;
        this.setSharedPrefValue(PREF_GCM_ID_KEY, gcmId);
        return this;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public UReactUser setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.setSharedPrefValue(PREF_PHONE_NUMBER_KEY, phoneNumber);
        return this;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        return this.toJSON(json);
    }

    public JSONObject toJSON(JSONObject json) throws JSONException {
        if (this.id == null) {
            this.setId(UUID.randomUUID().toString());
        }
        json.put("id", this.id);

        JSONObject data = new JSONObject();
        if (this.name != null && this.name.length() > 0) {
            data.put("name", this.name);
        }

        if (this.email != null && this.email.length() > 0) {
            data.put("email", this.email);
        }

        if (this.gcmId != null && this.gcmId.length() > 0) {
            data.put("gcmId", this.gcmId);
        }

        if (this.phoneNumber != null && this.phoneNumber.length() > 0) {
            data.put("phoneNumber", this.phoneNumber);
        }

        json.put("data", null);
        if (data.length() > 0) {
            json.put("data", data);
        }
        json.put("auto_data", Device.getInstance(context).toJSON());
        return json;
    }
}
