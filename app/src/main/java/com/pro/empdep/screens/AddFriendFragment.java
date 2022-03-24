package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pro.empdep.R;
import com.pro.empdep.adapters.AddFriendAdapter;
import com.pro.empdep.databinding.FragmentAddFriendBinding;
import com.pro.empdep.viewmodel.FriendsViewModel;

public class AddFriendFragment extends Fragment {

    View view;
    FragmentAddFriendBinding binding;
    FriendsViewModel viewModel;
    AddFriendAdapter adapter;


    public AddFriendFragment() {
        // Required empty public constructor
    }


    public static AddFriendFragment newInstance(String param1, String param2) {
        AddFriendFragment fragment = new AddFriendFragment();
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
        binding = FragmentAddFriendBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);



        getAllUsers();


        return view;
    }

    private void getAllUsers() {

        viewModel.getAllUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            adapter = new AddFriendAdapter(users, getContext());
            binding.searchFriendCycle.setAdapter(adapter);

        });

    }
}