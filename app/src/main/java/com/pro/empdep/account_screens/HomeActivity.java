package com.pro.empdep.account_screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pro.empdep.R;
import com.pro.empdep.screens.HomeFragmentDirections;

import me.ibrahimsn.lib.SmoothBottomBar;

public class HomeActivity extends AppCompatActivity {

    SmoothBottomBar bottomBar;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomBar = findViewById(R.id.bottomBar);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        bottomBar.setBarIndicatorRadius(22);



        //NavigationUI.setupActionBarWithNavController(this, navController);
        setupSmoothBottomMenu();

        boolean newUser = getIntent().getBooleanExtra("isNewUser", false);
        String phoneNumber = getIntent().getStringExtra("phoneNumber");


    }

    public void hideBottomBar() {
        bottomBar.setVisibility(View.GONE);
    }

    private void setupSmoothBottomMenu() {
        PopupMenu popupMenu = new PopupMenu(this, null);
        popupMenu.inflate(R.menu.bottom_nav_menu);
        Menu menu = popupMenu.getMenu();
        bottomBar.setupWithNavController(menu, navController);
    }

    public void showBottomBar() {
        bottomBar.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}