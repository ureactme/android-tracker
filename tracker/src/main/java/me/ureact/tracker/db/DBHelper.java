package me.ureact.tracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import me.ureact.tracker.Event;

/**
 * Created by Valeriy Palamarchuk on 6/1/16
 */
public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UReactMe.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Contract.EventEntry.TABLE_NAME + " (" +
                    Contract.EventEntry._ID + " INTEGER PRIMARY KEY," +
                    Contract.EventEntry.COLUMN_NAME_ENTRY_GUID + TEXT_TYPE + COMMA_SEP +
                    Contract.EventEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    Contract.EventEntry.COLUMN_NAME_ACTION + TEXT_TYPE + COMMA_SEP +
                    Contract.EventEntry.COLUMN_NAME_LABEL + TEXT_TYPE + COMMA_SEP +
                    Contract.EventEntry.COLUMN_NAME_VALUE + REAL_TYPE + COMMA_SEP +
                    Contract.EventEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    Contract.EventEntry.COLUMN_NAME_TAGS + TEXT_TYPE +
                    " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Contract.EventEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Add the new event to the DB
     */
    public void addEvent(Event event) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.EventEntry.COLUMN_NAME_ENTRY_GUID, event.getGuid());
        values.put(Contract.EventEntry.COLUMN_NAME_CATEGORY, event.getCategory());
        values.put(Contract.EventEntry.COLUMN_NAME_ACTION, event.getAction());
        values.put(Contract.EventEntry.COLUMN_NAME_LABEL, event.getLabel());
        values.put(Contract.EventEntry.COLUMN_NAME_VALUE, event.getValue());
        values.put(Contract.EventEntry.COLUMN_NAME_DATE, String.valueOf(event.getDate().getTime()));
        values.put(Contract.EventEntry.COLUMN_NAME_TAGS, hashMap2Json(event.getTags()));

        db.insert(Contract.EventEntry.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * Get the <code>ArrayList</code> of GUIDs that are not synchronized yet
     */
    public ArrayList<Event> getEvents() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Event> events = new ArrayList<>();
        try {
            String[] projection = {
                    Contract.EventEntry._ID,
                    Contract.EventEntry.COLUMN_NAME_ENTRY_GUID,
                    Contract.EventEntry.COLUMN_NAME_CATEGORY,
                    Contract.EventEntry.COLUMN_NAME_ACTION,
                    Contract.EventEntry.COLUMN_NAME_LABEL,
                    Contract.EventEntry.COLUMN_NAME_VALUE,
                    Contract.EventEntry.COLUMN_NAME_DATE,
                    Contract.EventEntry.COLUMN_NAME_TAGS,
            };

            String sortOrder = Contract.EventEntry.COLUMN_NAME_DATE + " DESC";
            Cursor c = db.query(Contract.EventEntry.TABLE_NAME, projection, null, null, null, null,
                    sortOrder);

            if (c.moveToFirst()) {
                do {
                    String guid = c.getString(1);
                    String category = c.getString(2);
                    String action = c.getString(3);
                    String label = c.getString(4);
                    double value = c.getDouble(5);
                    Date date = new Date(Long.parseLong(c.getString(6)));
                    String tags = c.getString(7);

                    Event event = new Event(category, action, label, guid, value, date,
                            json2HshMap(tags));
                    events.add(event);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return events;
    }

    /**
     * Delete all events with selected GUIDs from the DB
     */
    public void deleteEvents(ArrayList<String> guids) {
        SQLiteDatabase db = getReadableDatabase();
        try {
            String[] selectionArgs = new String[guids.size()];
            guids.toArray(selectionArgs);
            String selection = Contract.EventEntry.COLUMN_NAME_ENTRY_GUID + "=?";
            db.delete(Contract.EventEntry.TABLE_NAME, selection, selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    /**
     * Delete all events from the DB
     */
    public void deleteAllEvents() {
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.delete(Contract.EventEntry.TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /**
     * Add the new event to the DB in the background
     *
     * @param callback to receive result
     */
    public void addEventInBackground(final Event event, final AddEventCallback callback) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                addEvent(event);
                callback.onFinish();
                return null;
            }
        }.execute();
    }

    /**
     * Get the <code>ArrayList</code> of GUIDs that are not synchronized yet in the background
     *
     * @param callback to receive result
     */
    public void getEventsInBackground(final GetEventsCallback callback) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                ArrayList<Event> notSyncedEvents = getEvents();
                callback.onFinish(notSyncedEvents);
                return null;
            }
        }.execute();
    }

    /**
     * Delete all events in background
     */
    public void deleteAllEventsInBackground() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                deleteAllEvents();
                return null;
            }
        }.execute();
    }


    /**
     * Convert the <code>HashMap<String, String></code> to <code>String</code>
     *
     * @param map to convert
     * @return the string representation
     */
    private String hashMap2Json(HashMap<String, String> map) {
        return new JSONObject(map).toString();
    }

    /**
     * Convert the <code>String</code> representation of the {@link Event} to the
     * <code>HashMap<String, String></code>
     *
     * @param event string representation of the {@link Event}
     * @return <code>HashMap<String, String></code>
     * @throws JSONException if the <code>event == null</code>
     */
    private HashMap<String, String> json2HshMap(String event) throws JSONException {
        HashMap<String, String> map = new HashMap<>();
        JSONObject jObject = new JSONObject(event);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }
        return map;
    }
}