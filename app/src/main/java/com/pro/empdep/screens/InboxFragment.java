package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pro.empdep.R;
import com.pro.empdep.adapters.InboxAdapter;
import com.pro.empdep.databinding.FragmentInboxBinding;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.interfaces.MessageOpener;
import com.pro.empdep.model.Group;
import com.pro.empdep.model.User;
import com.pro.empdep.viewmodel.FriendsViewModel;
import com.pro.empdep.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class InboxFragment extends Fragment implements MessageOpener {

    View view;
    FragmentInboxBinding binding;
    NavController navController;
    FriendsViewModel viewModel;
    InboxAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Group> group = new ArrayList<>();

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
            FriendDialogueMenu menu = new FriendDialogueMenu(getActivity(), navHostFragment);
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

           //todo error when fresh person open because of list size null
            if (user.getFriendReq() != null && user.getGroupReq() != null) {
                SpannableString content = new SpannableString("New Request (" + String.valueOf(user.getFriendReq().size() + user.getGroupReq().size()) + ")");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                binding.newInboxRequest.setText(content);
                getInboxList(user);
            }else{
                SpannableString content = new SpannableString("New Request (0)");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                binding.newInboxRequest.setText(content);
                getInboxList(user);
            }
        });
    }

    private void getInboxList(User user) {

        viewModel.getInbox(user.getGroups()).observe(getViewLifecycleOwner(), groups -> {

            Log.d("INBOX", "getTotalPendingRequest: " + groups.size());
            Collections.sort(groups, (group1, group2) -> {
                Long timestamp1 = Long.parseLong(group1.getTimestamp());
                Long timestamp2 = Long.parseLong(group2.getTimestamp());

                return timestamp2.compareTo(timestamp1);
            });
            adapter = new InboxAdapter(groups, getContext(), this);
            binding.inboxCycle.setAdapter(adapter);


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


    @Override
    public void onGroupClick(String id) {


        NavDirections actions = InboxFragmentDirections.actionInboxFragmentToMessagesFragment(id);
        navController.navigate(actions);
        db.collection(Credentials.GROUP).document(id).update("seen_by", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid()));



    }
}