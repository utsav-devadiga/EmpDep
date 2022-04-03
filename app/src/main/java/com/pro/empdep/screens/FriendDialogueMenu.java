package com.pro.empdep.screens;

import android.app.Activity;
import android.app.AlertDialog;
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

public class FriendDialogueMenu {

    private final Activity activity;
    private AlertDialog view;
    MaterialCardView addFriend, createGroup;
    NavController navController;
    NavHostFragment navHostFragment;


    public FriendDialogueMenu(Activity activity,NavHostFragment navHostFragment) {
        this.activity = activity;
        this.navHostFragment = navHostFragment;
    }

    public void showMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.friend_menu_dialog, null));
        builder.setCancelable(true);
        view = builder.create();
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = view.getWindow().getAttributes();
        wmlp.windowAnimations = R.style.Animation_Design_BottomSheetDialog;
        view.show();

        navController = navHostFragment.getNavController();

        addFriend = view.findViewById(R.id.add_friend_btn);
        createGroup = view.findViewById(R.id.create_group_btn);



        addFriend.setOnClickListener(v -> {

            NavDirections actions = InboxFragmentDirections.actionInboxFragmentToAddFriendFragment();
            navController.navigate(actions);
            view.dismiss();
        });

        createGroup.setOnClickListener(v -> {
            NavDirections actions = InboxFragmentDirections.actionInboxFragmentToFriendBottomSheet();
            navController.navigate(actions);
            view.dismiss();
        });






    }


}
