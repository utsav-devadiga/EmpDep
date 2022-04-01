package com.pro.empdep.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pro.empdep.databinding.GroupCreationBottomSheetBinding;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.FriendList;
import com.pro.empdep.repo.GroupRepository;
import com.pro.empdep.viewmodel.GroupViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GroupCreationBottomSheet extends BottomSheetDialogFragment {
    ArrayList<String> groupMemberList = new ArrayList<>();
    GroupCreationBottomSheetBinding binding;
    GroupViewModel viewModel;
    View view;
    BottomSheetBehavior bottomSheetBehavior;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = GroupCreationBottomSheetBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(GroupViewModel.class);


        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);

        ViewGroup.LayoutParams layoutParams = binding.bottomSheet.getLayoutParams();


        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        binding.bottomSheet.setLayoutParams(layoutParams);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        binding.createGroupFab.setOnClickListener(view -> {
            String groupName = binding.groupNameTextInputLayout.getEditText().getText().toString().trim();
            if (groupName.equals("")) {
                binding.groupNameTextInputLayout.setError("Group name can't be empty");
            } else {
                //todo create group and proper validation
                //done in text-watcher
                createGroup(groupName);
            }
        });

        binding.groupNameTextInputLayout.getEditText().addTextChangedListener(textWatcher);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Credentials.FRIENDS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Credentials.FRIENDS, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        groupMemberList = gson.fromJson(json, type);

        if (groupMemberList == null) {
            //error saving
            Toast.makeText(getContext(), "Error Occurred, please try again!", Toast.LENGTH_SHORT).show();
            dismiss();
        } else {

        }
        Log.d("GROUP-BOTTOM-SHEET", "onViewCreated: " + groupMemberList.size());

    }

    private void createGroup(String name) {
        viewModel.validGroupName(name).observe(getViewLifecycleOwner(), validated -> {
            if (validated) {
                viewModel.groupCreated(groupMemberList, name).observe(getViewLifecycleOwner(), finished -> {
                    if (finished) {
                        dismiss();
                    } else {
                        binding.groupNameTextInputLayout.setError("Error Occurred, Please try later");
                    }
                });
            } else {
                binding.groupNameTextInputLayout.setError("Group name already exist!");
            }
        });

    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    //implement the TextWatcher callback listener
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // todo check for the same name in group collection
            viewModel.validGroupName(s.toString().trim()).observe(getViewLifecycleOwner(), validated -> {
                if (validated) {
                    binding.groupNameTextInputLayout.setError(null);
                } else {
                    binding.groupNameTextInputLayout.setError("Group name already exist!");
                }
            });
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}
