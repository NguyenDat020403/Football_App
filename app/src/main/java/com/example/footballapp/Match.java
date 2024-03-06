package com.example.footballapp;

import java.util.Comparator;

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
    public static Comparator<Match> timeComparator = new Comparator<Match>() {
        @Override
        public int compare(Match m1, Match m2) {
            // Sử dụng phương thức compareTo() của lớp Time để so sánh thời gian
            int time1 = convertToMinutes(m1.getTime());
            int time2 = convertToMinutes(m2.getTime());

            // So sánh thời gian của hai đối tượng Match và trả về kết quả
            return Integer.compare(time1, time2);
        }
        private int convertToMinutes(String time) {
            // Split chuỗi thời gian thành giờ và phút
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            // Chuyển đổi thành số phút
            return hours * 60 + minutes;
        }
    };

}
