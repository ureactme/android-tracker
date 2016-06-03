package me.ureact.tracker.db;

import java.util.ArrayList;

import me.ureact.tracker.Event;

/**
 * Created by Valeriy Palamarchuk on 6/1/16
 */
public interface GetEventsCallback {
    void onFinish(ArrayList<Event> events);
}
