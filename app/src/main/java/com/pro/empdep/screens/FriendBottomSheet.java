package com.pro.empdep.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.pro.empdep.R;
import com.pro.empdep.adapters.FriendsListAdapter;
import com.pro.empdep.databinding.FriendBottomSheetBinding;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.interfaces.FriendSelected;
import com.pro.empdep.model.FriendList;
import com.pro.empdep.model.User;
import com.pro.empdep.viewmodel.FriendsViewModel;

import java.util.ArrayList;

public class FriendBottomSheet extends BottomSheetDialogFragment implements FriendSelected {

    View view;
    FriendBottomSheetBinding binding;
    FriendsViewModel viewModel;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FriendsListAdapter adapter;
    ArrayList<String> groupMemberList = new ArrayList<>();
    BottomSheetBehavior BottomSheetBehavior;
    NavController navController;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = FriendBottomSheetBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);


        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        binding.createGroupFab.setOnClickListener(view -> {


            //save the list locally
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Credentials.FRIENDS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(groupMemberList);
            editor.putString(Credentials.FRIENDS, json);
            editor.apply();

            NavDirections actions = FriendBottomSheetDirections.actionFriendBottomSheetToGroupCreationBottomSheet();
            dismiss();
            navController.navigate(actions);
            groupMemberList.clear();


        });

        BottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);

        ViewGroup.LayoutParams layoutParams = binding.bottomSheet.getLayoutParams();


        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        binding.bottomSheet.setLayoutParams(layoutParams);
        BottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        getFriends();


        return view;
    }


    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void getFriends() {

        viewModel.getFriends().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                binding.noFriends.setVisibility(View.VISIBLE);
                binding.createGroupFab.setVisibility(View.GONE);
                binding.cycleHeader.setVisibility(View.GONE);
            } else {
                if (user.isEmpty()) {
                    binding.noFriends.setVisibility(View.VISIBLE);
                    binding.cycleHeader.setVisibility(View.GONE);
                    binding.createGroupFab.setVisibility(View.GONE);
                } else {
                    binding.noFriends.setVisibility(View.GONE);
                    binding.cycleHeader.setVisibility(View.VISIBLE);
                    adapter = new FriendsListAdapter(getContext(), user, this);
                    binding.friendCycle.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.friendCycle.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayout.VERTICAL));


                }
            }
        });

    }

    @Override
    public void onFriendSelected(String id) {
        if (groupMemberList.contains(id)) {
            groupMemberList.remove(id);
            Log.d("BOTTOM-SHEET", "onFriendSelected: " + id + " Removed");
        } else {
            groupMemberList.add(id);
            Log.d("BOTTOM-SHEET", "onFriendSelected: " + id + " Added");
        }


    }
}
