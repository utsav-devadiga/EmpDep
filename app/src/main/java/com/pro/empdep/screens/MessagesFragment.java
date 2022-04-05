package com.pro.empdep.screens;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.R;
import com.pro.empdep.account_screens.HomeActivity;
import com.pro.empdep.adapters.InboxAdapter;
import com.pro.empdep.adapters.MessageAdapter;
import com.pro.empdep.databinding.FragmentMessagesBinding;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.Group;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        //((AppCompatActivity) getActivity()).setSupportActionBar(binding.loginToolbar);
        //setHasOptionsMenu(true);

        binding.loginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) getActivity()).onBackPressed();
            }
        });

        binding.loginToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.exit:
                    //do sth here
                    Toast.makeText(getContext(), "exit", Toast.LENGTH_SHORT).show();
                case R.id.settings:
                    //do sth here
                    Toast.makeText(getContext(), "settings", Toast.LENGTH_SHORT).show();
            }
            return false;
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

        db.collection(Credentials.GROUP).document(groupId).get().addOnCompleteListener(task -> {
            Group group = task.getResult().toObject(Group.class);
            binding.loginToolbar.setTitle(group.getGroup_name());
            Glide.with(getContext()).asDrawable()
                    .load(group.getGroup_picture())
                    .into(new SimpleTarget<Drawable>(80,80) {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            binding.loginToolbar.setLogo(resource);
                        }
                    });

        });

    }
}