package com.example.focusstarttest;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CbrJson {
    @SerializedName("Date")
    private String date;

    private List<Valute> valutes = new ArrayList<>();

    public CbrJson() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Valute> getValutes() {
        return valutes;
    }

    public void addValute(Valute valute) {
        this.valutes.add(valute);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Date: ").append(getDate());
        for (int i = 0; i < valutes.size(); i++) {
            sb.append(" valute: { ").append(valutes.toString()).append(" }");
            if (i + 1 != valutes.size()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
