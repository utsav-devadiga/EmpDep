package com.pro.empdep.adapters;


import static com.pro.empdep.places.constants.AppConstant.API_KEY_PHOTO_REF;
import static com.pro.empdep.places.constants.AppConstant.BASE_URL;
import static com.pro.empdep.places.constants.AppConstant.PHOTO_REF;

import android.content.Context;
import android.media.Image;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.pro.empdep.R;
import com.pro.empdep.apiClients.FriendRequest;
import com.pro.empdep.apiClients.FriendRequestClient;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.NotificationBody;
import com.pro.empdep.model.NotificationContent;
import com.pro.empdep.model.User;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //todo get 3 type of list
    // 1) all the users in the server (to show all the users)
    // 2) all the friends in the current users account (to get the mutual)
    // 3) all the request in the current users request list (to get the existing request) && (if accepted remove the item from the list)

    List<User> users;
    Context context;
    View view;
    FirebaseAuth mAuth;
    User CurrentUser;
    FriendRequestClient friendRequestClient;

    public AddFriendAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        getCurrentUser(this.users);
        friendRequestClient = FriendRequest.getRetrofitInstance().create(FriendRequestClient.class);


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
          /*  case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_self_item, parent, false);
                viewHolder = new SelfViewHolder(view);
                break;*/
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_friend_item_accept_reject, parent, false);
                viewHolder = new ExistingFriendViewHolder(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_friend_item, parent, false);
                viewHolder = new NewFriendViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            //pending request
            case 1:
                ArrayList<String> mutualListCurrent = (ArrayList<String>) CurrentUser.getFriends();
                ArrayList<String> mutualList = (ArrayList<String>) users.get(position).getFriends();
                mutualListCurrent.retainAll(mutualList);

                ((ExistingFriendViewHolder) holder).username.setText(users.get(position).getUserName());

                if (mutualListCurrent.size() > 0) {
                    if (mutualListCurrent.size() == 1) {
                        ((ExistingFriendViewHolder) holder).mutual.setText("" + mutualListCurrent.size() + " mutual friend");
                    } else {
                        ((ExistingFriendViewHolder) holder).mutual.setText("" + mutualListCurrent.size() + " mutual friends");
                    }
                }

                Glide.with(context)
                        .load(users.get(position).getPhotoUrl())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(((ExistingFriendViewHolder) holder).profilePic);

                ((ExistingFriendViewHolder) holder).add.setOnClickListener(view -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(Credentials.USER).document(CurrentUser.getId()).update("friends", FieldValue.arrayUnion(users.get(holder.getAdapterPosition()).getId()));
                    db.collection(Credentials.USER).document(users.get(holder.getAdapterPosition()).getId()).update("friends", FieldValue.arrayUnion(CurrentUser.getId()));
                    db.collection(Credentials.USER).document(CurrentUser.getId()).update("friendReq", FieldValue.arrayRemove(users.get(holder.getAdapterPosition()).getId()));
                    removeItem(users.get(holder.getAdapterPosition()).getId());
                });

                ((ExistingFriendViewHolder) holder).no.setOnClickListener(view -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(Credentials.USER).document(CurrentUser.getId()).update("friendReq", FieldValue.arrayRemove(users.get(holder.getAdapterPosition()).getId()));
                    removeItem(users.get(holder.getAdapterPosition()).getId());
                });

                break;

            //new Users
            case 2:
                ((NewFriendViewHolder) holder).username.setText(users.get(position).getUserName());
                ArrayList<String> newMutualListCurrent = (ArrayList<String>) CurrentUser.getFriends();
                ArrayList<String> newMutualList = (ArrayList<String>) users.get(position).getFriends();
                newMutualListCurrent.retainAll(newMutualList);


                if (newMutualListCurrent.size() > 0) {
                    if (newMutualListCurrent.size() == 1) {
                        ((NewFriendViewHolder) holder).mutual.setText("" + newMutualListCurrent.size() + " mutual friend");
                    } else {
                        ((NewFriendViewHolder) holder).mutual.setText("" + newMutualListCurrent.size() + " mutual friends");
                    }
                }
                Glide.with(context)
                        .load(users.get(position).getPhotoUrl())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(((NewFriendViewHolder) holder).profilePic);

                ((NewFriendViewHolder) holder).addFriendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String deviceToken = users.get(holder.getAdapterPosition()).getDevice();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection(Credentials.USER).document(users.get(holder.getAdapterPosition()).getId()).update("friendReq", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid())).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                //todo send Notification

                                NotificationBody notificationBody = new NotificationBody(deviceToken, new NotificationContent(CurrentUser.getUserName() + " sent you a friend request!", ""));
                                friendRequestClient.sendFriendRequestNotification(notificationBody).enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        ((NewFriendViewHolder) holder).addFriendBtn.setEnabled(false);
                                        ((NewFriendViewHolder) holder).addFriendStatus.setText("Request sent");
                                        ((NewFriendViewHolder) holder).addFriendStatusIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_confirm));
                                        Log.d("NOTIFICATION", "onResponse: " + response);
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                if (users.get(position).getFriendReq().contains(CurrentUser.getId())) {
                    ((NewFriendViewHolder) holder).addFriendBtn.setEnabled(false);
                    ((NewFriendViewHolder) holder).addFriendStatus.setText("Request sent");
                    ((NewFriendViewHolder) holder).addFriendStatusIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_confirm));
                }


                break;

        }
    }

    @Override
    public int getItemCount() {
        Log.d("LIST-SIZE", "getItemCount: " + users.size());
        return users.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (CurrentUser.getFriendReq() != null && CurrentUser.getFriendReq().contains(users.get(position).getId())) {
            return 1;
        } else {
            return 2;
        }

    }

    public void getCurrentUser(List<User> users) {
        if (users != null) {
            for (User user : users) {
                if (user.getId().equals(mAuth.getCurrentUser().getUid())) {
                    CurrentUser = user;
                }
            }
        } else {
            CurrentUser = null;
        }

        Iterator itr = users.iterator();

        // Holds true till there is single element
        // remaining in the object
        while (itr.hasNext()) {
            // Remove elements smaller than 10 using
            // Iterator.remove()
            User user = (User) itr.next();
            if (user.getId().equals(mAuth.getCurrentUser().getUid())) {
                itr.remove();
            }
            if (CurrentUser.getFriends() != null && CurrentUser.getFriends().contains(user.getId())) {
                itr.remove();
            }

        }


    }


    public void removeItem(String id) {
        Iterator itr = users.iterator();

        // Holds true till there is single element
        // remaining in the object
        while (itr.hasNext()) {
            // Remove elements smaller than 10 using
            // Iterator.remove()
            User user = (User) itr.next();
            if (id.equals(user.getId())) {
                itr.remove();
            }

        }
        notifyDataSetChanged();
    }

    public static class NewFriendViewHolder extends RecyclerView.ViewHolder {

        TextView username, mutual, addFriendStatus;
        ImageView profilePic;
        MaterialCardView addFriendBtn;
        ImageView addFriendStatusIcon;


        public NewFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.friend_user_name);
            mutual = itemView.findViewById(R.id.friend_mutual);
            profilePic = itemView.findViewById(R.id.friend_image);
            addFriendBtn = itemView.findViewById(R.id.add_friend_btn);
            addFriendStatus = itemView.findViewById(R.id.add_friend_status_text);
            addFriendStatusIcon = itemView.findViewById(R.id.add_friend_status_image);

        }
    }

    public static class ExistingFriendViewHolder extends RecyclerView.ViewHolder {
        TextView username, mutual;
        ImageView profilePic;
        MaterialCardView add, no;

        public ExistingFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.friend_user_name);
            mutual = itemView.findViewById(R.id.friend_mutual);
            profilePic = itemView.findViewById(R.id.friend_image);
            add = itemView.findViewById(R.id.add_friend_accepted);
            no = itemView.findViewById(R.id.add_friend_rejected);

        }
    }


}
