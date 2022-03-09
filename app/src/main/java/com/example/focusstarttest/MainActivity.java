package com.example.focusstarttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final String cbrDailyJsonUrl = "https://www.cbr-xml-daily.ru/daily_json.js";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExecutorService pool = Executors.newSingleThreadExecutor();
        DownloadJson downloadJson = new DownloadJson(cbrDailyJsonUrl);

        try {
            String jsonResult = pool.submit(downloadJson).get();
            CbrJson cbrJson = jsonToCbrObject(jsonResult);
            Log.i("JSONResult", cbrJson.toString());

            try {
                JSONObject jsonObject = new JSONObject(jsonResult);
                JSONObject valute = (JSONObject) jsonObject.get("Valute");

                Log.i("JSON", valute.toString());
                /*for (int i = 0; i < valute.length(); i++) {
                    Log.i("JSON", valute.);
                }*/
                //Log.i("JSON Date", jsonObject.getString("Date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i("URL", jsonResult);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        pool.shutdown();
    }

    public CbrJson jsonToCbrObject(String json) {
        //List<T> result = new ArrayList<>();

        var jsonElement = (JsonElement) JsonParser.parseString(json);
        Log.i("JSONA", jsonElement.toString());
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        var result = gson.fromJson(json, CbrJson.class);

        try {
            JSONObject jo = new JSONObject(json).getJSONObject("Valute");
            Iterator<String> iterator = jo.keys();

            while (iterator.hasNext()) {
                String key = iterator.next();
                Valute valute = gson.fromJson(jo.get(key).toString(), Valute.class);
                valute.setTag(key);
                result.addValute(valute);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}