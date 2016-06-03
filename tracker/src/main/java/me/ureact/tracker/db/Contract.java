package me.ureact.tracker.db;

import android.provider.BaseColumns;

/**
 * Created by Valeriy Palamarchuk on 6/1/16
 */
public class Contract {
    public Contract() {
    }

    public static abstract class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String COLUMN_NAME_ENTRY_GUID = "guid";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_ACTION = "action";
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TAGS = "tags";
    }
}
