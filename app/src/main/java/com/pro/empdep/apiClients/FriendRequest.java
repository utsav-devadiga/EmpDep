package com.pro.empdep.apiClients;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pro.empdep.places.constants.AppConstant;

import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendRequest {


    public static Retrofit getRetrofitInstance() {

        try {

            OkHttpClient client = new OkHttpClient.Builder()

                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();

                        Request.Builder builder = originalRequest.newBuilder();

                       /* builder.header("Content-Type", "application/json");
                        builder.header("Authorization:", AppConstant.FCM_SERVER_KEY);*/
                        Request newRequest = builder.build();
                        Log.d("URL", "RETROFIT: " + newRequest.url());
                        return chain.proceed(newRequest);
                    }).build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            return new Retrofit.Builder()
                    .baseUrl(AppConstant.FCM_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}


