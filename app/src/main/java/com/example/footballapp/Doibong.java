package com.example.footballapp;

public class Doibong {
    public String Id;
    public String Stt;
    public String Name;

    public String Match;
    public String Win;
    public String Draw;
    public String Lose;
    public String Point;
    public String Photo;

    public Doibong(String id, String stt, String name, String match, String win, String draw, String lose, String point, String photo) {
        Id = id;
        Stt = stt;
        Name = name;
        Match = match;
        Win = win;
        Draw = draw;
        Lose = lose;
        Point = point;
        Photo = photo;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStt() {
        return Stt;
    }

    public void setStt(String stt) {
        Stt = stt;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMatch() {
        return Match;
    }

    public void setMatch(String match) {
        Match = match;
    }

    public String getWin() {
        return Win;
    }

    public void setWin(String win) {
        Win = win;
    }

    public String getDraw() {
        return Draw;
    }

    public void setDraw(String draw) {
        Draw = draw;
    }

    public String getLose() {
        return Lose;
    }

    public void setLose(String lose) {
        Lose = lose;
    }

    public String getPoint() {
        return Point;
    }

    public void setPoint(String point) {
        Point = point;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
