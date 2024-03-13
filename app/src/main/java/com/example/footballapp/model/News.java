package com.example.footballapp.model;

public class News {
    public String url;
    public String title;
    public String image;
    public String short_desc;

    public News(String url, String title, String image, String short_desc) {
        this.url = url;
        this.title = title;
        this.image = image;
        this.short_desc = short_desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }
}
