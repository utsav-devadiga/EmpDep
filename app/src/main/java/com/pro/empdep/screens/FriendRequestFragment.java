package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.pro.empdep.R;
import com.pro.empdep.adapters.FriendRequestAdapter;
import com.pro.empdep.adapters.RequestPagerAdapter;
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
    RequestPagerAdapter requestPagerAdapter;

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


        requestPagerAdapter = new RequestPagerAdapter(getActivity().getSupportFragmentManager(), getLifecycle());

        binding.pager.setAdapter(requestPagerAdapter);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("FRIENDS"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("GROUPS"));


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });

        return view;
    }

}