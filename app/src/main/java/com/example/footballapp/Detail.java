package com.example.footballapp;

public class Detail {
    private String photoPlayer;
    private String namePlayer;
    private String typePlayer;
    private String agePlayer;
    private String ratePlayer;

    public Detail(String photoPlayer, String namePlayer, String typePlayer, String agePlayer, String ratePlayer) {
        this.photoPlayer = photoPlayer;
        this.namePlayer = namePlayer;
        this.typePlayer = typePlayer;
        this.agePlayer = agePlayer;
        this.ratePlayer = ratePlayer;
    }

    public String getPhotoPlayer() {
        return photoPlayer;
    }

    public void setPhotoPlayer(String photoPlayer) {
        this.photoPlayer = photoPlayer;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public String getTypePlayer() {
        return typePlayer;
    }

    public void setTypePlayer(String typePlayer) {
        this.typePlayer = typePlayer;
    }

    public String getAgePlayer() {
        return agePlayer;
    }

    public void setAgePlayer(String agePlayer) {
        this.agePlayer = agePlayer;
    }

    public String getRatePlayer() {
        return ratePlayer;
    }

    public void setRatePlayer(String ratePlayer) {
        this.ratePlayer = ratePlayer;
    }
}
