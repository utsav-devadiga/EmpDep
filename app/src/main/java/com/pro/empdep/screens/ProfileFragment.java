package com.pro.empdep.screens;


import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.pro.empdep.R;
import com.pro.empdep.account_screens.LoginActivity;
import com.pro.empdep.databinding.FragmentProfileBinding;


import com.pro.empdep.model.User;
import com.pro.empdep.viewmodel.UserViewModel;


import java.util.Date;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    public static String TAG = "PROFILE-FRAGMENT";
    View view;
    UserViewModel userViewModel;


    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        userViewModel.getUserFromFirebase();
        userViewModel.mUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "onChanged: " + user.toString());

                Glide.with(requireContext())
                        .load(user.getPhotoUrl())
                        .transform(new FitCenter(), new RoundedCorners(28))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.profileImage);

            }
        });


        binding.logout.setOnClickListener(view -> {
            logoutCurrentUser();
        });


        backPress();


        return view;
    }

    private void logoutCurrentUser() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent logOutIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(logOutIntent);
        requireActivity().finish();
        Log.d(TAG, "logoutCurrentUser: User logged out at " + new Date());
    }

    private void backPress() {
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        callback.setEnabled(true);


    }


}