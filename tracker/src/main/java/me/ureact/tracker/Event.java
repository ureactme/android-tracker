package me.ureact.tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by pappacena on 13/12/15.
 */
public class Event {
    private String category;
    private String action;
    private String label;
    private String guid;
    private double value;
    private Date date;
    private HashMap<String, String> tags;

    public Event() {
        this.value = 1;
        this.tags = new HashMap<>();
        this.date = new Date();
        this.guid = UUID.randomUUID().toString();
    }

    public Event(String category, String action, String label, String guid, double value,
                 Date date, HashMap<String, String> tags) {
        this.category = category;
        this.action = action;
        this.label = label;
        this.guid = guid;
        this.value = value;
        this.date = date;
        this.tags = tags;
    }

    public String getGuid() {
        return guid;
    }

    public Date getDate() {
        return date;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public Event addMetadata(String tag, String value) {
        this.tags.put(tag, value);
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Event setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Event setAction(String action) {
        this.action = action;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Event setLabel(String label) {
        this.label = label;
        return this;
    }

    public double getValue() {
        return this.value;
    }

    public Event setValue(double value) {
        this.value = value;
        return this;
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

    @Override
    public String toString() {
        return "Event[" + category + " – " + action + " - " + label + " – " + value + "], "
                + guid + ", " + date + ", tags=" + tags;
    }
}
