package com.example.footballapp.model;

import java.util.Comparator;

public class Player {
    public String STT;
    public String Name;
    public String CLB;
    public String SoBanThang;
    public String Photo;

    public Player(String STT, String name, String CLB, String soBanThang, String photo) {
        this.STT = STT;
        Name = name;
        this.CLB = CLB;
        SoBanThang = soBanThang;
        Photo = photo;
    }
    public static Comparator<Player> positionComparator = new Comparator<Player>() {
        @Override
        public int compare(Player p1, Player p2) {
            // Sắp xếp theo thứ tự của vị trí (position)
            return Integer.compare(Integer.parseInt(p1.getSTT()), Integer.parseInt(p2.getSTT()));
        }
    };
    public String getSTT() {
        return STT;
    }

    public String getName() {
        return Name;
    }

    public String getCLB() {
        return CLB;
    }

    public String getSoBanThang() {
        return SoBanThang;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }
}
