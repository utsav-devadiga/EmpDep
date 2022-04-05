package com.pro.empdep.screens;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.card.MaterialCardView;
import com.pro.empdep.R;
import com.pro.empdep.firebase.Credentials;

public class UserPreference {

    private final Activity activity;
    private AlertDialog view;
    MaterialCardView beaches, parks, mountains, history;
    NavController navController;
    Intent homeIntent;


    public UserPreference(Activity activity, Intent homeIntent) {
        this.activity = activity;
        this.homeIntent = homeIntent;

    }

    public void showUserPref() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.user_preference, null));
        builder.setCancelable(true);
        view = builder.create();
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = view.getWindow().getAttributes();
        wmlp.windowAnimations = R.style.Animation_Design_BottomSheetDialog;
        view.show();


        beaches = view.findViewById(R.id.beaches);
        parks = view.findViewById(R.id.parks);
        mountains = view.findViewById(R.id.mountains);
        history = view.findViewById(R.id.monuments);


        beaches.setOnClickListener(v -> {
            SharedPreferences prefs = activity.getSharedPreferences(Credentials.USER_PREF, Context.MODE_PRIVATE);
            prefs.edit().putString("place", "beaches").apply();
            activity.startActivity(homeIntent);
            activity.finish();
            view.dismiss();
        });

        parks.setOnClickListener(v -> {
            SharedPreferences prefs = activity.getSharedPreferences(Credentials.USER_PREF, Context.MODE_PRIVATE);
            prefs.edit().putString("place", "parks").apply();
            activity.startActivity(homeIntent);
            activity.finish();
            view.dismiss();
        });
        mountains.setOnClickListener(v -> {
            SharedPreferences prefs = activity.getSharedPreferences(Credentials.USER_PREF, Context.MODE_PRIVATE);
            prefs.edit().putString("place", "mountains").apply();
            activity.startActivity(homeIntent);
            activity.finish();
            view.dismiss();
        });
        history.setOnClickListener(v -> {
            SharedPreferences prefs = activity.getSharedPreferences(Credentials.USER_PREF, Context.MODE_PRIVATE);
            prefs.edit().putString("place", "monuments").apply();
            activity.startActivity(homeIntent);
            activity.finish();
            view.dismiss();
        });


    }


}

