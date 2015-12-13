package us.utrak.tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by pappacena on 13/12/15.
 */
public class Event {
    private String metric;
    private double value;
    private HashMap<String, String> tags;

    public Event() {
        this.metric = "";
        this.value = 0;
        this.tags = new HashMap<String, String>();
    }

    public Event setMetric(String metric) {
        this.metric = metric;
        return this;
    }

    public Event setValue(double value) {
        this.value = value;
        return this;
    }

    public Event addTag(String tag, String value) {
        this.tags.put(tag, value);
        return this;
    }

    public String getMetric() {
        return this.metric;
    }

    public double getValue() {
        return this.value;
    }

    public HashMap<String, String> getTags() {
        return this.tags;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("metric", this.getMetric());
        json.put("value", this.getValue());
        json.put("metadata", new JSONObject(this.getTags()));
        return json;
    }
}
