package com.example.footballapp.model;

import java.util.Comparator;

public class Match {
    public String ID;
    public String Time;
    public String nameHome;
    public String nameAway;
    public String scoreHome;
    public String scoreAway;

    public String photoHome;
    public String photoAway;

    public String BallPossessionHome;
    public String BallPossessionAway;
    public String ShotsTotalHome;
    public String ShotsTotalAway;
    public String ShotsOnGoalHome;
    public String ShotsOnGoalAway;

    public String FoulsHome;
    public String FoulsAway;
    public String CornersHome;
    public String CornersAway;
    public String OffsidesHome;
    public String OffsidesAway;

    public Match(String ID, String time, String nameHome, String nameAway, String scoreHome, String scoreAway, String photoHome, String photoAway, String ballPossessionHome, String ballPossessionAway, String shotsTotalHome, String shotsTotalAway, String shotsOnGoalHome, String shotsOnGoalAway, String foulsHome, String foulsAway, String cornersHome, String cornersAway, String offsidesHome, String offsidesAway) {
        this.ID = ID;
        Time = time;
        this.nameHome = nameHome;
        this.nameAway = nameAway;
        this.scoreHome = scoreHome;
        this.scoreAway = scoreAway;
        this.photoHome = photoHome;
        this.photoAway = photoAway;
        BallPossessionHome = ballPossessionHome;
        BallPossessionAway = ballPossessionAway;
        ShotsTotalHome = shotsTotalHome;
        ShotsTotalAway = shotsTotalAway;
        ShotsOnGoalHome = shotsOnGoalHome;
        ShotsOnGoalAway = shotsOnGoalAway;
        FoulsHome = foulsHome;
        FoulsAway = foulsAway;
        CornersHome = cornersHome;
        CornersAway = cornersAway;
        OffsidesHome = offsidesHome;
        OffsidesAway = offsidesAway;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getNameHome() {
        return nameHome;
    }

    public void setNameHome(String nameHome) {
        this.nameHome = nameHome;
    }

    public String getNameAway() {
        return nameAway;
    }

    public void setNameAway(String nameAway) {
        this.nameAway = nameAway;
    }

    public String getScoreHome() {
        return scoreHome;
    }

    public void setScoreHome(String scoreHome) {
        this.scoreHome = scoreHome;
    }

    public String getScoreAway() {
        return scoreAway;
    }

    public void setScoreAway(String scoreAway) {
        this.scoreAway = scoreAway;
    }

    public String getPhotoHome() {
        return photoHome;
    }

    public void setPhotoHome(String photoHome) {
        this.photoHome = photoHome;
    }

    public String getPhotoAway() {
        return photoAway;
    }

    public void setPhotoAway(String photoAway) {
        this.photoAway = photoAway;
    }

    public String getBallPossessionHome() {
        return BallPossessionHome;
    }

    public void setBallPossessionHome(String ballPossessionHome) {
        BallPossessionHome = ballPossessionHome;
    }

    public String getBallPossessionAway() {
        return BallPossessionAway;
    }

    public void setBallPossessionAway(String ballPossessionAway) {
        BallPossessionAway = ballPossessionAway;
    }

    public String getShotsTotalHome() {
        return ShotsTotalHome;
    }

    public void setShotsTotalHome(String shotsTotalHome) {
        ShotsTotalHome = shotsTotalHome;
    }

    public String getShotsTotalAway() {
        return ShotsTotalAway;
    }

    public void setShotsTotalAway(String shotsTotalAway) {
        ShotsTotalAway = shotsTotalAway;
    }

    public String getShotsOnGoalHome() {
        return ShotsOnGoalHome;
    }

    public void setShotsOnGoalHome(String shotsOnGoalHome) {
        ShotsOnGoalHome = shotsOnGoalHome;
    }

    public String getShotsOnGoalAway() {
        return ShotsOnGoalAway;
    }

    public void setShotsOnGoalAway(String shotsOnGoalAway) {
        ShotsOnGoalAway = shotsOnGoalAway;
    }

    public String getFoulsHome() {
        return FoulsHome;
    }

    public void setFoulsHome(String foulsHome) {
        FoulsHome = foulsHome;
    }

    public String getFoulsAway() {
        return FoulsAway;
    }

    public void setFoulsAway(String foulsAway) {
        FoulsAway = foulsAway;
    }

    public String getCornersHome() {
        return CornersHome;
    }

    public void setCornersHome(String cornersHome) {
        CornersHome = cornersHome;
    }

    public String getCornersAway() {
        return CornersAway;
    }

    public void setCornersAway(String cornersAway) {
        CornersAway = cornersAway;
    }

    public String getOffsidesHome() {
        return OffsidesHome;
    }

    public void setOffsidesHome(String offsidesHome) {
        OffsidesHome = offsidesHome;
    }

    public String getOffsidesAway() {
        return OffsidesAway;
    }

    public void setOffsidesAway(String offsidesAway) {
        OffsidesAway = offsidesAway;
    }

    public static Comparator<Match> getTimeComparator() {
        return timeComparator;
    }

    public static void setTimeComparator(Comparator<Match> timeComparator) {
        Match.timeComparator = timeComparator;
    }

    public static Comparator<Match> timeComparator = new Comparator<Match>() {
        @Override
        public int compare(Match m1, Match m2) {
            int time1 = convertToMinutes(m1.getTime());
            int time2 = convertToMinutes(m2.getTime());

            return Integer.compare(time1, time2);
        }
        private int convertToMinutes(String time) {
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours * 60 + minutes;
        }
    };

}
