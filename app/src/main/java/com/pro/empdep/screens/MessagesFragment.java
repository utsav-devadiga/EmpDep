package com.pro.empdep.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.R;
import com.pro.empdep.adapters.MessageAdapter;
import com.pro.empdep.databinding.FragmentMessagesBinding;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.Message;
import com.pro.empdep.viewmodel.MessagesViewModel;

import java.util.ArrayList;


public class MessagesFragment extends Fragment {

    String groupId = "";
    FragmentMessagesBinding binding;
    View view;
    MessagesViewModel viewModel;
    MessageAdapter adapter;
    ArrayList<Message> messagesList = new ArrayList<>();

    public MessagesFragment() {
        // Required empty public constructor
    }


    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
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
        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        adapter = new MessageAdapter(messagesList, getContext());
        binding.messageCycle.setAdapter(adapter);

        binding.buttonGchatSend.setOnClickListener(v -> {
            String message = binding.editGchatMessage.getText().toString().trim();
            if (!message.equals("")) {
                sendNormalMessage(groupId, message);
            }

        });


        return view;
    }

    private void sendNormalMessage(String groupId, String message) {
        viewModel.sendNormalMessage(groupId, message).observe(getViewLifecycleOwner(), messageSent -> {
            if (messageSent) {
                binding.editGchatMessage.setText(null);

            } else {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMessage(String id) {
        if (!groupId.equals("")) {
            viewModel.getMessages(id).observe(getViewLifecycleOwner(), messages -> {

                if (messages != null) {
                    if (!messages.isEmpty()) {
                        messagesList.clear();
                        messagesList.addAll(messages);
                        adapter.notifyDataSetChanged();
                        binding.messageCycle.scrollToPosition(messagesList.size() - 1);

                    } else {
                        Toast.makeText(getContext(), "No Messages " + groupId, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No Messages! " + groupId, Toast.LENGTH_SHORT).show();
                }


            });
        } else {
            Toast.makeText(getContext(), "Something went wrong! id: " + groupId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupId = MessagesFragmentArgs.fromBundle(getArguments()).getGroupId();
        Log.d("GROUP", "onViewCreated: " + groupId);
        getMessage(groupId);

    }
}