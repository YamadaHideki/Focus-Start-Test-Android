package com.example.focusstarttest;

import android.provider.BaseColumns;

public class NotesCbr {
    public static final class NotesJson implements BaseColumns {
        public static final String TABLE_NAME = "cbr_json";
        public static final String VALUTE_TAG = "valute_tag";
        public static final String VALUTE_ID = "valute_id";
        public static final String VALUTE_NUM_CODE = "valute_num_code";
        public static final String VALUTE_CHAR_CODE = "valute_char_code";
        public static final String VALUTE_NOMINAL = "valute_nominal";
        public static final String VALUTE_NAME = "valute_name";
        public static final String VALUTE_VALUE = "valute_value";
        public static final String VALUTE_PREVIOUS = "valute_previous";

        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INTEGER = "INTEGER";

        public static final String CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " +
                VALUTE_TAG + " " + TYPE_TEXT + ", " + VALUTE_ID + " " + TYPE_TEXT + ", " +
                VALUTE_NUM_CODE + " " + TYPE_TEXT + ", " + VALUTE_CHAR_CODE + " " + TYPE_TEXT + ", " +
                VALUTE_NOMINAL + " " + TYPE_INTEGER + ", " + VALUTE_NAME + " " + TYPE_TEXT + ", " +
                VALUTE_VALUE + " " + TYPE_TEXT + ", " + VALUTE_PREVIOUS + " " + TYPE_TEXT + ")";

        public static final String DROP_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
