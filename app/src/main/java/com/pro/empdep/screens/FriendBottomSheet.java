package com.pro.empdep.screens;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.pro.empdep.R;
import com.pro.empdep.adapters.FriendsListAdapter;
import com.pro.empdep.databinding.FriendBottomSheetBinding;
import com.pro.empdep.interfaces.FriendSelected;
import com.pro.empdep.model.User;
import com.pro.empdep.viewmodel.FriendsViewModel;

import java.util.ArrayList;

public class FriendBottomSheet extends BottomSheetDialogFragment implements FriendSelected {

    View view;
    FriendBottomSheetBinding binding;
    FriendsViewModel viewModel;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FriendsListAdapter adapter;
    ArrayList<String> groupMemberList = new ArrayList<>();
    BottomSheetBehavior BottomSheetBehavior;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = FriendBottomSheetBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);


        binding.createGroupFab.setOnClickListener(view -> {

        dismiss();
        });

        getFriends();


        return view;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        groupMemberList.clear();
    }

    public void getFriends() {

        viewModel.getFriends().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                binding.noFriends.setVisibility(View.VISIBLE);
            } else {
                if (user.isEmpty()) {
                    binding.noFriends.setVisibility(View.VISIBLE);
                    binding.cycleHeader.setVisibility(View.GONE);
                } else {
                    binding.noFriends.setVisibility(View.GONE);
                    binding.cycleHeader.setVisibility(View.VISIBLE);
                    adapter = new FriendsListAdapter(getContext(), user, this);
                    binding.friendCycle.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.friendCycle.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayout.VERTICAL));


                }
            }
        });

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Override
    public void onFriendSelected(String id) {
        if (groupMemberList.contains(id)) {
            groupMemberList.remove(id);
            Log.d("BOTTOM-SHEET", "onFriendSelected: " + id + " Removed");
        } else {
            groupMemberList.add(id);
            Log.d("BOTTOM-SHEET", "onFriendSelected: " + id + " Added");
        }


    }
}
