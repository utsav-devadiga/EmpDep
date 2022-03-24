package com.pro.empdep.account_screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.pro.empdep.R;
import com.pro.empdep.account_screens.MainActivity;

public class NoInternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        Button tryagain = findViewById(R.id.button_tryAgain);

        tryagain.setOnClickListener(view -> {
            Intent tryIntent = new Intent(this, MainActivity.class);
            startActivity(tryIntent);
            finish();

        });

    }


}