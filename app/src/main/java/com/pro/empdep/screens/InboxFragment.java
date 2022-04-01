package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pro.empdep.R;
import com.pro.empdep.databinding.FragmentInboxBinding;
import com.pro.empdep.model.User;
import com.pro.empdep.viewmodel.FriendsViewModel;
import com.pro.empdep.viewmodel.UserViewModel;


public class InboxFragment extends Fragment {

    View view;
    FragmentInboxBinding binding;
    NavController navController;
    FriendsViewModel viewModel;


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

        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        binding.fabAddFriends.setOnClickListener(view -> {
            FriendDialogueMenu menu = new FriendDialogueMenu(getActivity(),navHostFragment);
            menu.showMenu();
        });



        binding.newInboxRequest.setOnClickListener(view1 -> {
            NavDirections actions = InboxFragmentDirections.actionInboxFragmentToFriendRequestFragment();
            navController.navigate(actions);
        });

        //handle Navigation
        backPress();
        getTotalPendingRequest();

        return view;
    }

    private void getTotalPendingRequest() {
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {

            SpannableString content = new SpannableString("New Request (" + String.valueOf(user.getFriendReq().size() + user.getGroupReq().size()) + ")");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            binding.newInboxRequest.setText(content);
        });
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