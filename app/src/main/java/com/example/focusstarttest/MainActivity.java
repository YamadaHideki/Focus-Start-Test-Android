package com.example.focusstarttest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final String cbrDailyJsonUrl = "https://www.cbr-xml-daily.ru/daily_json.js";
    private NotesDBHelper dbHelper;
    private SQLiteDatabase database;
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new NotesDBHelper(this);
        database = dbHelper.getWritableDatabase();
        database.delete(NotesCbr.NotesJson.TABLE_NAME, null, null);

        pool.execute(this::updateInfoFromCbr);

        /*dbHelper = new NotesDBHelper(this);
        database = dbHelper.getWritableDatabase();*/


        //DownloadJson downloadJson = new DownloadJson(cbrDailyJsonUrl);

        //preferences.edit().putString("date_json", "2022-03-10T11:30:00+03:00").apply();


        Cursor cursor = database.query(NotesCbr.NotesJson.TABLE_NAME, null,
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String valuteTag = cursor.getString(cursor.getColumnIndex(NotesCbr.NotesJson.VALUTE_TAG));
            @SuppressLint("Range") String valuteName = cursor.getString(cursor.getColumnIndex(NotesCbr.NotesJson.VALUTE_NAME));
            Log.i("DB", "TAG: " + valuteTag + ", NAME: " + valuteName);
        }
        cursor.close();
        //Log.i("db", preferences.getString("date_json", null));





        //try {
            //String jsonResult = pool.submit(downloadJson).get();
            //preferences.edit().

        //CbrJson cbrJson = jsonToCbrObject(jsonResult);
            //Log.i("JSONResult", cbrJson.toString());

            //try {
                //JSONObject jsonObject = new JSONObject(jsonResult);
                //JSONObject valute = (JSONObject) jsonObject.get("Valute");

                //Log.i("JSON", valute.toString());
                /*for (int i = 0; i < valute.length(); i++) {
                    Log.i("JSON", valute.);
                }*/
                //Log.i("JSON Date", jsonObject.getString("Date"));
            /*} catch (JSONException e) {
                e.printStackTrace();
            }*/

            //Log.i("URL", jsonResult);
        /*} catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }*/

        pool.shutdown();
    }

    public void updateInfoFromCbr() {
        preferences.edit().clear().apply();
        preferences.edit().putString("date_json_update", "2022-03-10T11:30:00+03:00").apply();
        //String dateJsonUpdateString = preferences.getString("date_json_update", null);
        /*try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

            Date currentJsonUpdateDate = format.parse("2022-03-09T11:30:00+03:00");
            Date today = new Date();
            assert currentJsonUpdateDate != null;
            int oneDayInMilliseconds = 1000 * 60 * 60 * 24;
            Date nextJsonUpdateDate =
                    new Date(currentJsonUpdateDate.getTime() + (oneDayInMilliseconds));

            if (today.getTime() > nextJsonUpdateDate.getTime()) {
                Log.i("KRIOD", "Update");
            } else {
                Log.i("KRIOD", "Cache");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.i("KRIO", date.toString());
        if (dateJsonUpdateString != null) {
            Log.i("KRIO", dateJsonUpdateString);
        } else {
            Log.i("KRIO", "NULL");
        }*/

        try {
            String dateJsonUpdate = preferences.getString("date_json_update", null);
            if (dateJsonUpdate != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                //Date currentJsonUpdateDate = format.parse(dateJsonUpdate);
                Date currentJsonUpdateDate = format.parse("2022-03-09T11:30:00+03:00");
                Date today = new Date();
                int oneDayInMilliseconds = 1000 * 60 * 60 * 24;
                Date nextJsonUpdateDate =
                        new Date(currentJsonUpdateDate.getTime() + (oneDayInMilliseconds));

                if (today.getTime() > nextJsonUpdateDate.getTime()) {
                    String json = pool.submit(new DownloadJson(cbrDailyJsonUrl)).get();
                    JSONObject jo = new JSONObject(json);
                    JSONObject joValute = jo.getJSONObject("Valute");
                    preferences.edit().putString("date_json_update", jo.getString("Date")).apply();

                    Iterator<String> joIterator = joValute.keys();
                    while (joIterator.hasNext()) {
                        String key = joIterator.next();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(NotesCbr.NotesJson.VALUTE_TAG, key);
                        contentValues.put(NotesCbr.NotesJson.VALUTE_ID, joValute.getString("ID"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_NUM_CODE, joValute.getString("NumCode"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_CHAR_CODE, joValute.getString("CharCode"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_NOMINAL, joValute.getString("Nominal"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_NAME, joValute.getString("Name"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_VALUE, joValute.getString("Value"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_PREVIOUS, joValute.getString("Previous"));

                        int id = (int) database.insertWithOnConflict(NotesCbr.NotesJson.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                        Log.i("DB", String.valueOf(id));
                        if (id == -1) {
                            Cursor cursor = database.query(NotesCbr.NotesJson.TABLE_NAME, new String[]{"id"},
                                    NotesCbr.NotesJson.VALUTE_TAG + " = ?", new String[]{key}, null, null, null);
                            String updateId = cursor.getString(0);
                            cursor.close();
                            database.update(NotesCbr.NotesJson.TABLE_NAME, contentValues, "_id=?", new String[]{updateId});
                        }
                    }
                    Log.i("KRIOD", "Update");
                } else {
                    Log.i("KRIOD", "Cache");
                }
            } else {
                Log.i("DB", "Not empty date_json");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } /*catch (ExecutionException | InterruptedException | JSONException | ParseException | NullPointerException e) {
            e.printStackTrace();
        }*/ finally {

        }
    }
}