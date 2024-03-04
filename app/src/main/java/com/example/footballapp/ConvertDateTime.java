package com.example.footballapp;

public class ConvertDateTime {
    private String convertedTime;
    private String convertedDate;

    public ConvertDateTime(String convertedTime, String convertedDate) {
        this.convertedTime = convertedTime;
        this.convertedDate = convertedDate;
    }

    public String getConvertedTime() {
        return convertedTime;
    }

    public void setConvertedTime(String convertedTime) {
        this.convertedTime = convertedTime;
    }

    public String getConvertedDate() {
        return convertedDate;
    }

    public void setConvertedDate(String convertedDate) {
        this.convertedDate = convertedDate;
    }
}
