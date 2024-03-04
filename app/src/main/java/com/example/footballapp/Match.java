package com.example.footballapp;

public class Match {
    public String Time;
    public String nameHome;
    public String nameAway;
    public String scoreHome;
    public String scoreAway;

    public String photoHome;
    public String photoAway;

    public String getTime() {
        return Time;
    }

    public String getNameHome() {
        return nameHome;
    }

    public String getNameAway() {
        return nameAway;
    }

    public String getScoreHome() {
        return scoreHome;
    }

    public String getScoreAway() {
        return scoreAway;
    }

    public String getPhotoHome() {
        return photoHome;
    }

    public String getPhotoAway() {
        return photoAway;
    }

    public Match(String time, String nameHome, String nameAway, String scoreHome, String scoreAway, String photoHome, String photoAway) {
        Time = time;
        this.nameHome = nameHome;
        this.nameAway = nameAway;
        this.scoreHome = scoreHome;
        this.scoreAway = scoreAway;
        this.photoHome = photoHome;
        this.photoAway = photoAway;
    }
}
