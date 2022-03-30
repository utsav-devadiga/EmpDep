package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pro.empdep.R;
import com.pro.empdep.adapters.FriendRequestAdapter;
import com.pro.empdep.databinding.FragmentFriendRequestBinding;
import com.pro.empdep.databinding.FragmentInboxBinding;
import com.pro.empdep.model.User;
import com.pro.empdep.viewmodel.FriendsViewModel;
import com.pro.empdep.viewmodel.UserViewModel;

import java.util.List;


public class FriendRequestFragment extends Fragment {

    FragmentFriendRequestBinding binding;
    View view;
    FriendRequestAdapter adapter;
    FriendsViewModel viewModel;

    public FriendRequestFragment() {
        // Required empty public constructor
    }


    public static FriendRequestFragment newInstance(String param1, String param2) {

        FriendRequestFragment fragment = new FriendRequestFragment();
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
        binding = FragmentFriendRequestBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);


        viewModel.getPendingFriendRequest().observe(getViewLifecycleOwner(), friendRequest -> {
            User currentUser;
            viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
                adapter = new FriendRequestAdapter(friendRequest, getContext(), user);
                binding.friendRequestCycle.setAdapter(adapter);
            });

        });

        return view;
    }


}