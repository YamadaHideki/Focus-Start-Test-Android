package com.example.focusstarttest;

import android.content.ContentProvider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class DbController {
    private static volatile DbController INSTANCE;
    private NotesDBHelper dbHelper;
    private SQLiteDatabase database;

    private DbController (Context context) {
        dbHelper = new NotesDBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public static DbController getInstance() {
        synchronized (DbController.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            } else {
                return null;
            }
        }
    }

    public static void init(Context context) {
        if (INSTANCE == null) {
            synchronized (DbController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DbController(context);
                }
            }
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
