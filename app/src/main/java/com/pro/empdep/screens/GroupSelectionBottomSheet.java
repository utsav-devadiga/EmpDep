package com.pro.empdep.screens;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pro.empdep.R;
import com.pro.empdep.adapters.InboxAdapter;
import com.pro.empdep.adapters.SuggestionPlaceGroupAdapter;
import com.pro.empdep.databinding.GroupSelectionBottomSheetBinding;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.User;
import com.pro.empdep.places.interfaces.SuggestedPlaceToGroup;
import com.pro.empdep.viewmodel.FriendsViewModel;
import com.pro.empdep.viewmodel.MessagesViewModel;
import com.pro.empdep.viewmodel.UserViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class GroupSelectionBottomSheet extends BottomSheetDialogFragment implements SuggestedPlaceToGroup {

    BottomSheetBehavior bottomSheetBehavior;
    GroupSelectionBottomSheetBinding binding;
    View view;
    FriendsViewModel viewModel;
    MessagesViewModel messagesViewModel;
    NavController navController;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SuggestionPlaceGroupAdapter adapter;
    String placeid="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = GroupSelectionBottomSheetBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);

        ViewGroup.LayoutParams layoutParams = binding.bottomSheet.getLayoutParams();
        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        binding.bottomSheet.setLayoutParams(layoutParams);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        binding.sendToGroupFab.setOnClickListener(view -> {
            //open inbox and send message directly
        });


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Credentials.GROUP, Context.MODE_PRIVATE);
        placeid = sharedPreferences.getString("place", "");
        getTotalPendingRequest();

        return view;
    }


    private void getTotalPendingRequest() {
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {

            SpannableString content = new SpannableString("New Request (" + String.valueOf(user.getFriendReq().size() + user.getGroupReq().size()) + ")");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

            getInboxList(user);
        });
    }

    private void getInboxList(User user) {

        viewModel.getInbox(user.getGroups()).observe(getViewLifecycleOwner(), groups -> {

            Log.d("INBOX", "getTotalPendingRequest: " + groups.size());
            adapter = new SuggestionPlaceGroupAdapter(groups, getContext(), this,placeid);
            binding.groupCycle.setAdapter(adapter);
        });
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void onGroupSelected(String groupId, String placeid) {

        messagesViewModel.sendPlaceSuggestion(groupId, placeid);


        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavDirections actions = GroupSelectionBottomSheetDirections.actionGroupSelectionBottomSheetToMessagesFragment(groupId);
        navController.navigate(actions);

    }
}
