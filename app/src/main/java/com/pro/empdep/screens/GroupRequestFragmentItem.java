package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pro.empdep.R;
import com.pro.empdep.adapters.FriendRequestAdapter;
import com.pro.empdep.databinding.FragmentGroupRequestItemBinding;
import com.pro.empdep.model.User;
import com.pro.empdep.viewmodel.FriendsViewModel;

public class GroupRequestFragmentItem extends Fragment {


    FragmentGroupRequestItemBinding binding;
    View view;


    public GroupRequestFragmentItem() {
        // Required empty public constructor
    }


    public static GroupRequestFragmentItem newInstance(String param1, String param2) {
        GroupRequestFragmentItem fragment = new GroupRequestFragmentItem();
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
        binding = FragmentGroupRequestItemBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        /*viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);


        viewModel.getPendingFriendRequest().observe(getViewLifecycleOwner(), friendRequest -> {
            User currentUser;
            viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
                adapter = new FriendRequestAdapter(friendRequest, getContext(), user);
                binding.friendRequestCycle.setAdapter(adapter);
            });

        });*/
        return view;
    }
}