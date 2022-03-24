package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pro.empdep.R;
import com.pro.empdep.databinding.FragmentInboxBinding;


public class InboxFragment extends Fragment {

    View view;
    FragmentInboxBinding binding;
    NavController navController;


    public InboxFragment() {
        // Required empty public constructor
    }


    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
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
        // Inflate the layout for this fragment
        binding = FragmentInboxBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavDirections actions = InboxFragmentDirections.actionInboxFragmentToAddFriendFragment();

        binding.fabAddFriends.setOnClickListener(view -> navController.navigate(actions));

        //handle Navigation
        backPress();

        return view;
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