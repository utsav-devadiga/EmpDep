package com.pro.empdep.account_screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.color.DynamicColors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pro.empdep.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private final String TAG = "MainActivity";
    private LottieAnimationView splashAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);


        setContentView(R.layout.activity_main);
        splashAnimation = findViewById(R.id.splash_animation);


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            splashScreen.setKeepOnScreenCondition(() -> true);
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homeIntent);
            finish();
        } else {
            splashScreen.setKeepOnScreenCondition(() -> false);
            showSuperSplashScreen();
        }


    }

    private void showSuperSplashScreen() {
        splashAnimation.playAnimation();

        splashAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

}
