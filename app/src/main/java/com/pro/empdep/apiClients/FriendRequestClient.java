package com.pro.empdep.apiClients;



import com.google.gson.JsonObject;
import com.pro.empdep.model.NotificationBody;
import com.pro.empdep.places.constants.AppConstant;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface FriendRequestClient {

    @Headers({
            "Authorization:"+AppConstant.FCM_SERVER_KEY ,
            "Content-Type: application/json"
    })
    @POST("send")
    Call<JsonObject> sendFriendRequestNotification(@Body NotificationBody json);
}
