package com.example.focusstarttest;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Valute {

    private String tag;
    @SerializedName("ID")
    private String id;
    @SerializedName("NumCode")
    private String numCode;
    @SerializedName("CharCode")
    private String charCode;
    @SerializedName("Nominal")
    private String nominal;
    @SerializedName("Name")
    private String name;
    @SerializedName("Value")
    private String value;
    @SerializedName("Previous")
    private String previous;

    public Valute() {

    }

    public Valute(String tag, String id, String numCode, String charCode, String nominal, String name, String value, String previous) {
        this.tag = tag;
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
        this.previous = previous;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getId() {
        return id;
    }

    public String getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPrevious() {
        return previous;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("tag: " + tag + ", id: " + id);
        return sb.toString();
    }
}
