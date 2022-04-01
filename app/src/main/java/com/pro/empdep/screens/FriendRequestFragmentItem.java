package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pro.empdep.R;
import com.pro.empdep.adapters.FriendRequestAdapter;
import com.pro.empdep.databinding.FragmentFriendRequestItemBinding;
import com.pro.empdep.model.User;
import com.pro.empdep.viewmodel.FriendsViewModel;


public class FriendRequestFragmentItem extends Fragment {

    View view;
    FragmentFriendRequestItemBinding binding;
    FriendsViewModel viewModel;
    FriendRequestAdapter adapter;


    public FriendRequestFragmentItem() {
        // Required empty public constructor
    }

    public static FriendRequestFragmentItem newInstance(String param1, String param2) {
        FriendRequestFragmentItem fragment = new FriendRequestFragmentItem();
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
        binding = FragmentFriendRequestItemBinding.inflate(inflater, container, false);


        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        viewModel.getPendingFriendRequest().observe(getViewLifecycleOwner(), friendRequest -> {
            viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
                adapter = new FriendRequestAdapter(friendRequest, getContext(), user);
                binding.friendRequestCycle.setAdapter(adapter);
            });

        });
        view = binding.getRoot();
        return view;
    }
}