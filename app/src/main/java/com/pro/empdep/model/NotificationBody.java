package com.pro.empdep.model;

import com.google.gson.annotations.SerializedName;

public class NotificationBody {

    @SerializedName("to")
    private String token;

    @SerializedName("notification")
    private NotificationContent notificationContent;

    public NotificationBody(String token, NotificationContent notificationContent) {
        this.token = token;
        this.notificationContent = notificationContent;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNotificationContent(NotificationContent notificationContent) {
        this.notificationContent = notificationContent;
    }
}

