package me.ureact.tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import me.ureact.tracker.exceptions.InvalidTagName;

/**
 * Created by pappacena on 13/12/15.
 */
public class Event {
    private String category;
    private String action;
    private String label;
    private double value;
    private Date date;
    private HashMap<String, String> tags;

    public Event() {
        this.value = 1;
        this.tags = new HashMap<String, String>();
        this.date = new Date();
    }

    public Event setCategory(String category) {
        this.category = category;
        return this;
    }

    public Event setAction(String action) {
        this.action = action;
        return this;
    }

    public Event setLabel(String label) {
        this.label = label;
        return this;
    }

    public Event setValue(double value) {
        this.value = value;
        return this;
    }

    public Event addMetadata(String tag, String value) {
        this.tags.put(tag, value);
        return this;
    }

    public String getCategory() {
        return category;
    }

    public String getAction() {
        return action;
    }

    public String getLabel() {
        return label;
    }

    public double getValue() {
        return this.value;
    }

    public HashMap<String, String> getMetadata() {
        return this.tags;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        json.put("date", df.format(this.date));
        json.put("category", this.getCategory());
        json.put("action", this.getAction());
        json.put("label", this.getLabel());
        json.put("value", this.getValue());

        json.put("data", null);
        if (this.getMetadata().size() > 0) {
            json.put("data", new JSONObject(this.getMetadata()));
        }
        return json;
    }
}
