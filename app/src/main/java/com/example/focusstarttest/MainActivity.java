package com.example.focusstarttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private final String CBR_DAILY_JSON_URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private NotesDBHelper dbHelper;
    private SQLiteDatabase database;
    private final ExecutorService POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private SharedPreferences preferences;
    private RecyclerView valuteList;
    private ValutesAdapter valutesAdapter;
    //private DbController DB_CONTROLLER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new NotesDBHelper(this);
        database = dbHelper.getWritableDatabase();

        valuteList = findViewById(R.id.rv_valutes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                    valuteList.setLayoutManager(layoutManager);
        valuteList.setHasFixedSize(true);
        Map<Integer, Map<String ,String>> map = getDbInfo();
        valutesAdapter = new ValutesAdapter(map, 30);
                    valuteList.setAdapter(valutesAdapter);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //database.delete(NotesCbr.NotesJson.TABLE_NAME, null, null);

        POOL.execute(this::updateInfoFromCbr);

        /*dbHelper = new NotesDBHelper(this);
        database = dbHelper.getWritableDatabase();*/


        //DownloadJson downloadJson = new DownloadJson(cbrDailyJsonUrl);

        //preferences.edit().putString("date_json", "2022-03-10T11:30:00+03:00").apply();



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

        //pool.shutdown();
    }

    public String downloadJsonFromUrl(String pUrl) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            while (line != null) {
                result.append(line);
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        Log.i("json", "result");
        return result.toString();
    }

    public Map<Integer, Map<String, String>> getDbInfo() {
        int i = 0;
        Map<Integer, Map<String, String>> result = new HashMap<>();

        Cursor cursor = database.query(NotesCbr.NotesJson.TABLE_NAME, null,
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
            @SuppressLint("Range") String valuteName = cursor.getString(cursor.getColumnIndex(NotesCbr.NotesJson.VALUTE_NAME));
            @SuppressLint("Range") String valuteValue = cursor.getString(cursor.getColumnIndex(NotesCbr.NotesJson.VALUTE_VALUE));
            @SuppressLint("Range") String valuteNominal = cursor.getString(cursor.getColumnIndex(NotesCbr.NotesJson.VALUTE_NOMINAL));
            //Log.i("DB", "ID: " + id + ", TAG: " + valuteTag + ", NAME: " + valuteName);

            map.put(NotesCbr.NotesJson.VALUTE_NAME, valuteName);
            map.put(NotesCbr.NotesJson.VALUTE_NOMINAL, valuteNominal);
            map.put(NotesCbr.NotesJson.VALUTE_VALUE, valuteValue);
            result.put(i, map);
            i++;
        }
        cursor.close();
        return result;
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

                    //Future<String> jsonFuture = pool.submit(new DownloadJson(cbrDailyJsonUrl));
                    String json = downloadJsonFromUrl(CBR_DAILY_JSON_URL);

                    JSONObject jo = new JSONObject(json);

                    JSONObject joValute = jo.getJSONObject("Valute");
                    preferences.edit().putString("date_json_update", jo.getString("Date")).apply();

                    Iterator<String> joIterator = joValute.keys();
                    while (joIterator.hasNext()) {
                        String key = joIterator.next();

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(NotesCbr.NotesJson.VALUTE_TAG, key);
                        contentValues.put(NotesCbr.NotesJson.VALUTE_ID, joValute.getJSONObject(key).getString("ID"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_NUM_CODE, joValute.getJSONObject(key).getString("NumCode"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_CHAR_CODE, joValute.getJSONObject(key).getString("CharCode"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_NOMINAL, joValute.getJSONObject(key).getString("Nominal"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_NAME, joValute.getJSONObject(key).getString("Name"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_VALUE, joValute.getJSONObject(key).getString("Value"));
                        contentValues.put(NotesCbr.NotesJson.VALUTE_PREVIOUS, joValute.getJSONObject(key).getString("Previous"));

                        /*int id = (int) database.insertWithOnConflict(NotesCbr.NotesJson.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                        Log.i("DB_ID", String.valueOf(id));
                        if (id == -1) {
                            Cursor cursor = database.query(NotesCbr.NotesJson.TABLE_NAME, new String[]{"id"},
                                    NotesCbr.NotesJson.VALUTE_TAG + " = ?", new String[]{key}, null, null, null);
                            String updateId = cursor.getString(0);
                            cursor.close();
                            int updateProcessID = database.update(NotesCbr.NotesJson.TABLE_NAME, contentValues, "_id=?", new String[]{updateId});
                            Log.i("DB_UPDATE_PID", String.valueOf(updateProcessID));
                        }*/
                        //Log.i("DB_UPDATE", key);
                        int updateProcessId = database.update(NotesCbr.NotesJson.TABLE_NAME, contentValues,
                                NotesCbr.NotesJson.VALUTE_TAG + " = ?", new String[]{key});
                        //Log.i("DB_UPDATE_PID", "KEY: " + key + " | " + updateProcessId);
                        if (updateProcessId == 0) {
                            int insertProcessId = (int) database.insertWithOnConflict(NotesCbr.NotesJson.TABLE_NAME,
                                    null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                            //Log.i("DB_INSERT_PID", String.valueOf(insertProcessId));
                        }

                    }
                    Log.i("DB_LOG", "Update");
                } else {
                    Log.i("DB_LOG", "Use cache");
                }
            } else {
                Log.i("DB", "Empty date_json");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } /*catch (ExecutionException | InterruptedException | JSONException | ParseException | NullPointerException e) {
            e.printStackTrace();
        }*/ finally {
            getDbInfo();
        }
    }
}