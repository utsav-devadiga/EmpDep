package com.pro.empdep.model;

import com.google.gson.annotations.SerializedName;

public class NotificationContent {


    @SerializedName("title")
    String title;

    @SerializedName("body")
    String body;

    public NotificationContent(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

