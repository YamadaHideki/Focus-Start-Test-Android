package com.example.focusstarttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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

            try {
                JSONObject jsonObject = new JSONObject(jsonResult);
                JSONObject valute = (JSONObject) jsonObject.get("Valute");

                Log.i("JSON", valute.toString());
                /*for (int i = 0; i < valute.length(); i++) {
                    Log.i("JSON", valute.getString("ID"));
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
}