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
    private String metric;
    private double value;
    private Date date;
    private HashMap<String, String> tags;

    public Event() {
        this.metric = "";
        this.value = 0;
        this.tags = new HashMap<String, String>();
        this.date = new Date();
    }

    public Event setMetric(String metric) {
        this.metric = metric;
        return this;
    }

    public Event setValue(double value) {
        this.value = value;
        return this;
    }

    public Event addMetadata(String tag, String value) throws InvalidTagName {
        if(tag.equals("userid")) {
            throw new InvalidTagName("userid tagname is reserved and cannot be used");
        }
        this.tags.put(tag, value);
        return this;
    }

    public String getMetric() {
        return this.metric;
    }

    public double getValue() {
        return this.value;
    }

    public HashMap<String, String> addMetadata() {
        return this.tags;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        json.put("date", df.format(this.date));
        json.put("metric", this.getMetric());
        json.put("value", this.getValue());
        json.put("metadata", new JSONObject(this.addMetadata()));
        return json;
    }
}
