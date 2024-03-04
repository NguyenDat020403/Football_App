package com.example.footballapp;

public class Doibong {
    public String Stt;
    public String Name;

    public String Match;
    public String Win;
    public String Draw;
    public String Lose;
    public String Point;
    public String Photo;

    public Doibong(String stt, String name, String match, String win, String draw, String lose, String point,String photo) {
        Stt = stt;
        Name = name;
        Match = match;
        Win = win;
        Draw = draw;
        Lose = lose;
        Point = point;
        Photo = photo;
    }

    public String getPhoto() {
        return Photo;
    }

    public String getStt() {
        return Stt;
    }

    public String getName() {
        return Name;
    }

    public String getMatch() {
        return Match;
    }

    public String getWin() {
        return Win;
    }

    public String getDraw() {
        return Draw;
    }

    public String getLose() {
        return Lose;
    }

    public String getPoint() {
        return Point;
    }
}
