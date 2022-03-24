package com.pro.empdep.model;

import android.graphics.drawable.Drawable;

public class Places {
    String name;
    int img;
    String rate;
    String rating;
    String location;

    public Places(String name, int img, String rate, String rating, String location) {
        this.name = name;
        this.img = img;
        this.rate = rate;
        this.rating = rating;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
