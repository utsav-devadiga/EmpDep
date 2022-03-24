package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pro.empdep.R;
import com.pro.empdep.databinding.FragmentUserInfoBinding;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.User;
import com.pro.empdep.viewmodel.UserViewModel;

import java.util.Objects;


public class UserInfoFragment extends Fragment {


    View view;
    FragmentUserInfoBinding binding;
    UserViewModel userViewModel;
    String phone_number;
    NavController navController;
    String device="";

    public UserInfoFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                if (loading) {
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                device = task.getResult();
            }else{
                device = "";
            }
            Log.d("DEVICE-TOKEN", "onCreateView: "+device);
        });
        binding.userInfoNextBtn.setOnClickListener(view -> {


            binding.usernameTextInputLayout.setError(null);
            String username = binding.usernameTextInputLayout.getEditText().getText().toString().trim();
            if (!username.equals("")) {
                //TODO gender
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                User user = new User(phone_number, username, mAuth.getCurrentUser().getUid(), "MALE", mAuth.getCurrentUser().getPhotoUrl().toString(),device);
                userViewModel.saveUserInFirebase(user);
                NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
                navController = navHostFragment.getNavController();
                navController.navigateUp();
            } else {
                binding.usernameTextInputLayout.setError("Invalid Username");
                hideLoading();
            }

        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        binding.phoneTextInputLayout.getEditText().setText(user.getPhoneNumber());

    }

    public void showLoading() {
        binding.userInfoLoader.setVisibility(View.VISIBLE);
        binding.userInfoNextBtnText.setVisibility(View.GONE);
    }

    public void hideLoading() {
        binding.userInfoLoader.setVisibility(View.GONE);
        binding.userInfoNextBtnText.setVisibility(View.VISIBLE);
    }
}