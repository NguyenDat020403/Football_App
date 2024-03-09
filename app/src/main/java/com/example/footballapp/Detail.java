package com.example.footballapp;

public class Detail {
    public String photoPlayer;
    public String namePlayer;
    public String typePlayer;
    public String agePlayer;
    public String numberPlayer;

    public Detail(String photoPlayer, String namePlayer, String typePlayer, String agePlayer, String numberPlayer) {
        this.photoPlayer = photoPlayer;
        this.namePlayer = namePlayer;
        this.typePlayer = typePlayer;
        this.agePlayer = agePlayer;
        this.numberPlayer = numberPlayer;
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

    public String getNumberPlayer() {
        return numberPlayer;
    }

    public void setNumberPlayer(String numberPlayer) {
        this.numberPlayer = numberPlayer;
    }
}
