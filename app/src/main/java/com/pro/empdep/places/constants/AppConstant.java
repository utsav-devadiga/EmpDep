package com.pro.empdep.places.constants;

import android.os.Build;

import com.pro.empdep.BuildConfig;

public class AppConstant {

    public static final String BASE_URL = "https://maps.googleapis.com/";
    public static final String API_KEY = BuildConfig.MAPS_API_KEY;
    public static final String PHOTO_REF = "maps/api/place/photo?maxwidth=100&maxheight=100&photo_reference=";
    public static final String API_KEY_PHOTO_REF = "&key=" + BuildConfig.MAPS_API_KEY;
    public static final String FCM_BASE_URL = "https://fcm.googleapis.com/fcm/";
    public static final String FCM_SERVER_KEY = "key="+BuildConfig.FCM_KEY;
    public static final String THINGS_TODO_QUERY = "Things to do near ";
}
