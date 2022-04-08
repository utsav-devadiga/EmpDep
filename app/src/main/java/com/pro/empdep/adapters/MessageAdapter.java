package com.pro.empdep.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.R;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.Group;
import com.pro.empdep.model.Message;
import com.pro.empdep.model.User;
import com.pro.empdep.places.adapters.OpeningHourAdapter;
import com.pro.empdep.places.adapters.PlacesApiAdapter;
import com.pro.empdep.places.constants.AppConstant;
import com.pro.empdep.places.model.PlacesModel;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Message> messageList;
    Context context;
    FirebaseAuth mAuth;
    String current_user_id = "";
    FirebaseFirestore db;
    String groupId = "";

    public MessageAdapter(List<Message> messageList, Context context, String groupId) {
        this.messageList = messageList;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_incoming_normal, parent, false);
                viewHolder = new IncomingNormalViewHolder(view);
                break;

            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_outgoing_normal, parent, false);
                viewHolder = new OutgoingNormalViewHolder(view);
                break;

            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_continue_incoming_normal, parent, false);
                viewHolder = new IncomingNormalContinueViewHolder(view);
                break;

            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggested_place_incoming, parent, false);
                viewHolder = new IncomingSuggestedViewHolder(view);
                break;
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest_place_outgoing, parent, false);
                viewHolder = new OutGoingSuggestedViewHolder(view);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType) {
            case 0:
                ((IncomingNormalViewHolder) holder).message.setText(messageList.get(position).getMessage());

                ((IncomingNormalViewHolder) holder).time.setText(getDate(Long.parseLong(messageList.get(position).getTimestamp())));

                db.collection(Credentials.USER).document(messageList.get(position).getSent_by()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = task.getResult().toObject(User.class);
                        ((IncomingNormalViewHolder) holder).username.setText(user.getUserName());

                        Glide.with(context)
                                .load(user.getPhotoUrl())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(((IncomingNormalViewHolder) holder).user_pic);

                    }
                });
                break;

            case 2:
                ((IncomingNormalContinueViewHolder) holder).message.setText(messageList.get(position).getMessage());

                ((IncomingNormalContinueViewHolder) holder).time.setText(getDate(Long.parseLong(messageList.get(position).getTimestamp())));
                break;
            case 1:
                ((OutgoingNormalViewHolder) holder).message.setText(messageList.get(position).getMessage());
                ((OutgoingNormalViewHolder) holder).time.setText(getDate(Long.parseLong(messageList.get(position).getTimestamp())));
                break;

            case 3:

                if (messageList.get(position).getMessage_type() == Credentials.MESSAGES_PLACE_VOTE) {
                    ((IncomingSuggestedViewHolder) holder).yes.setVisibility(View.VISIBLE);
                    ((IncomingSuggestedViewHolder) holder).no.setVisibility(View.VISIBLE);
                    ((IncomingSuggestedViewHolder) holder).voted.setVisibility(View.GONE);
                    ((IncomingSuggestedViewHolder) holder).startvote.setVisibility(View.GONE);

                } else {
                    Log.d("MESSAGE", "onBindViewHolder: " + groupId);
                    db.collection(Credentials.GROUP).document(groupId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Group group = task.getResult().toObject(Group.class);
                            if (current_user_id.equals(group.getCreated_by())) {
                                ((IncomingSuggestedViewHolder) holder).startvote.setVisibility(View.VISIBLE);
                                ((IncomingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                                ((IncomingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                            }else{
                                ((IncomingSuggestedViewHolder) holder).startvote.setVisibility(View.GONE);
                                ((IncomingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                                ((IncomingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                                ((IncomingSuggestedViewHolder) holder).voted.setVisibility(View.GONE);
                            }
                        }
                    });


                }


                ((IncomingSuggestedViewHolder) holder).time.setText(getDate(Long.parseLong(messageList.get(position).getTimestamp())));


                db.collection(Credentials.USER).document(messageList.get(position).getSent_by()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = task.getResult().toObject(User.class);
                        ((IncomingSuggestedViewHolder) holder).username.setText(user.getUserName());

                        Glide.with(context)
                                .load(user.getPhotoUrl())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(((IncomingSuggestedViewHolder) holder).user_pic);

                    }
                });

                ((IncomingSuggestedViewHolder) holder).yes.setOnClickListener(view -> {
                    ((IncomingSuggestedViewHolder) holder).voted.setVisibility(View.VISIBLE);
                    ((IncomingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                    ((IncomingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("vote_yes", FieldValue.increment(1));
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("total_votes", FieldValue.increment(1));
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("voting_id", FieldValue.arrayUnion(current_user_id));

                });

                ((IncomingSuggestedViewHolder) holder).no.setOnClickListener(view -> {
                    ((IncomingSuggestedViewHolder) holder).voted.setVisibility(View.VISIBLE);
                    ((IncomingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                    ((IncomingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("vote_no", FieldValue.increment(1));
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("total_votes", FieldValue.increment(1));
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("voting_id", FieldValue.arrayUnion(current_user_id));

                });
                ((IncomingSuggestedViewHolder) holder).startvote.setOnClickListener(view -> {
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("message_type", Credentials.MESSAGES_PLACE_VOTE);
                    ((IncomingSuggestedViewHolder) holder).yes.setVisibility(View.VISIBLE);
                    ((IncomingSuggestedViewHolder) holder).no.setVisibility(View.VISIBLE);
                    ((IncomingSuggestedViewHolder) holder).startvote.setVisibility(View.GONE);

                });

                if (messageList.get(position).getVoting_id().contains(current_user_id)) {
                    ((IncomingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                    ((IncomingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                    ((IncomingSuggestedViewHolder) holder).startvote.setVisibility(View.GONE);
                    ((IncomingSuggestedViewHolder) holder).voted.setVisibility(View.VISIBLE);
                }


                //places
                // Specify the fields to return.
                final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.ADDRESS);

                // Construct a request object, passing the place ID and fields array.
                final FetchPlaceRequest request = FetchPlaceRequest.newInstance(messageList.get(position).getMessage(), placeFields);

                // Initialize Places.
                Places.initialize(context, AppConstant.API_KEY);

                // Create a new Places client instance.
                PlacesClient placesClient = Places.createClient(context);
                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    Place place = response.getPlace();


                    ((IncomingSuggestedViewHolder) holder).placename.setText(place.getName());
                    ((IncomingSuggestedViewHolder) holder).placeaddress.setText(place.getAddress());

                    // Get the photo metadata.
                    final List<PhotoMetadata> metadata = place.getPhotoMetadatas();

                    if (metadata == null || metadata.isEmpty()) {
                        Log.w("PLACES", "No photo metadata.");
                    }


                    final PhotoMetadata photoMetadata = metadata.get(0);

                    // Get the attribution text.
                    final String attributions = photoMetadata.getAttributions();

                    // Create a FetchPhotoRequest.
                    final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .build();
                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();

                        ((IncomingSuggestedViewHolder) holder).placeimage.setImageBitmap(bitmap);

                    });


                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        final ApiException apiException = (ApiException) exception;
                        Log.e("PLACES", "Place not found: " + exception.getMessage());
                        final int statusCode = apiException.getStatusCode();
                        // TODO: Handle error with given status code.
                    }
                });
                break;

            case 4:
                if (messageList.get(position).getMessage_type() == Credentials.MESSAGES_PLACE_VOTE) {
                    ((OutGoingSuggestedViewHolder) holder).yes.setVisibility(View.VISIBLE);
                    ((OutGoingSuggestedViewHolder) holder).no.setVisibility(View.VISIBLE);
                    ((OutGoingSuggestedViewHolder) holder).voted.setVisibility(View.GONE);
                    ((OutGoingSuggestedViewHolder) holder).startvote.setVisibility(View.GONE);

                } else {
                    Log.d("MESSAGE", "onBindViewHolder: " + groupId);
                    db.collection(Credentials.GROUP).document(groupId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Group group = task.getResult().toObject(Group.class);
                            if (current_user_id.equals(group.getCreated_by())) {
                                ((OutGoingSuggestedViewHolder) holder).startvote.setVisibility(View.VISIBLE);
                                ((OutGoingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                                ((OutGoingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                            }else{
                                ((OutGoingSuggestedViewHolder) holder).startvote.setVisibility(View.GONE);
                                ((OutGoingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                                ((OutGoingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                                ((OutGoingSuggestedViewHolder) holder).voted.setVisibility(View.GONE);
                            }
                        }
                    });


                }


                ((OutGoingSuggestedViewHolder) holder).time.setText(getDate(Long.parseLong(messageList.get(position).getTimestamp())));


                ((OutGoingSuggestedViewHolder) holder).yes.setOnClickListener(view -> {
                    ((OutGoingSuggestedViewHolder) holder).voted.setVisibility(View.VISIBLE);
                    ((OutGoingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                    ((OutGoingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("vote_yes", FieldValue.increment(1));
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("total_votes", FieldValue.increment(1));
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("voting_id", FieldValue.arrayUnion(current_user_id));

                });

                ((OutGoingSuggestedViewHolder) holder).no.setOnClickListener(view -> {
                    ((OutGoingSuggestedViewHolder) holder).voted.setVisibility(View.VISIBLE);
                    ((OutGoingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                    ((OutGoingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("vote_no", FieldValue.increment(1));
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("total_votes", FieldValue.increment(1));
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("voting_id", FieldValue.arrayUnion(current_user_id));

                });
                ((OutGoingSuggestedViewHolder) holder).startvote.setOnClickListener(view -> {
                    db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(messageList.get(position).getMessage_id()).update("message_type", Credentials.MESSAGES_PLACE_VOTE);
                    ((OutGoingSuggestedViewHolder) holder).yes.setVisibility(View.VISIBLE);
                    ((OutGoingSuggestedViewHolder) holder).no.setVisibility(View.VISIBLE);
                    ((OutGoingSuggestedViewHolder) holder).startvote.setVisibility(View.GONE);

                });

                if (messageList.get(position).getVoting_id().contains(current_user_id)) {
                    ((OutGoingSuggestedViewHolder) holder).yes.setVisibility(View.GONE);
                    ((OutGoingSuggestedViewHolder) holder).no.setVisibility(View.GONE);
                    ((OutGoingSuggestedViewHolder) holder).startvote.setVisibility(View.GONE);
                    ((OutGoingSuggestedViewHolder) holder).voted.setVisibility(View.VISIBLE);
                }


                //places
                // Specify the fields to return.
                final List<Place.Field> placeFields1 = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.ADDRESS);

                // Construct a request object, passing the place ID and fields array.
                final FetchPlaceRequest request1 = FetchPlaceRequest.newInstance(messageList.get(position).getMessage(), placeFields1);

                // Initialize Places.
                Places.initialize(context, AppConstant.API_KEY);

                // Create a new Places client instance.
                PlacesClient placesClient1 = Places.createClient(context);
                placesClient1.fetchPlace(request1).addOnSuccessListener((response) -> {
                    Place place = response.getPlace();


                    ((OutGoingSuggestedViewHolder) holder).placename.setText(place.getName());
                    ((OutGoingSuggestedViewHolder) holder).placeaddress.setText(place.getAddress());

                    // Get the photo metadata.
                    final List<PhotoMetadata> metadata = place.getPhotoMetadatas();

                    if (metadata == null || metadata.isEmpty()) {
                        Log.w("PLACES", "No photo metadata.");
                    }


                    final PhotoMetadata photoMetadata = metadata.get(0);

                    // Get the attribution text.
                    final String attributions = photoMetadata.getAttributions();

                    // Create a FetchPhotoRequest.
                    final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .build();
                    placesClient1.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();

                        ((OutGoingSuggestedViewHolder) holder).placeimage.setImageBitmap(bitmap);

                    });


                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        final ApiException apiException = (ApiException) exception;
                        Log.e("PLACES", "Place not found: " + exception.getMessage());
                        final int statusCode = apiException.getStatusCode();
                        // TODO: Handle error with given status code.
                    }
                });
                break;


        }


    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        Calendar now = Calendar.getInstance();
        cal.setTimeInMillis(time);

        if (cal.get(Calendar.DAY_OF_YEAR) < now.get(Calendar.DAY_OF_YEAR)) {
            String date = DateFormat.format("dd/MM/yy", cal).toString();
            return date;
        } else {
            String date = DateFormat.format("hh:mm", cal).toString();
            return date;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getMessage_type() == Credentials.MESSAGES_NORMAL && current_user_id.equals(messageList.get(position).getSent_by())) {
            return 1;
        }  if (position != 0 && messageList.get(position).getSent_by().equals(messageList.get(position - 1).getSent_by()) && messageList.get(position).getMessage_type() == Credentials.MESSAGES_NORMAL) {
            return 2;
        }  if (messageList.get(position).getMessage_type() == Credentials.MESSAGES_PLACE_VOTE || messageList.get(position).getMessage_type() == Credentials.MESSAGES_PLACE_SUGGESTION) {
            if (!current_user_id.equals(messageList.get(position).getSent_by())) {
                return 3;
            } else {
                return 4;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public static class IncomingNormalViewHolder extends RecyclerView.ViewHolder {
        TextView time, username, message;
        ImageView user_pic;

        public IncomingNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.text_gchat_timestamp_other);
            username = itemView.findViewById(R.id.text_gchat_user_other);
            message = itemView.findViewById(R.id.text_gchat_message_other);
            user_pic = itemView.findViewById(R.id.image_gchat_profile);

        }
    }

    public static class OutgoingNormalViewHolder extends RecyclerView.ViewHolder {
        TextView time, message;

        public OutgoingNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.text_gchat_timestamp_me);
            message = itemView.findViewById(R.id.text_gchat_message_me);

        }
    }

    public static class IncomingNormalContinueViewHolder extends RecyclerView.ViewHolder {
        TextView time, message;

        public IncomingNormalContinueViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.text_gchat_timestamp_other);
            message = itemView.findViewById(R.id.text_gchat_message_other);

        }
    }


    public static class IncomingSuggestedViewHolder extends RecyclerView.ViewHolder {
        TextView placename, username, time, placeaddress;
        ImageView placeimage, user_pic;
        MaterialCardView startvote, yes, no, voted;

        public IncomingSuggestedViewHolder(@NonNull View itemView) {
            super(itemView);
            placename = itemView.findViewById(R.id.text_gchat_message_other);
            username = itemView.findViewById(R.id.text_gchat_user_other);
            time = itemView.findViewById(R.id.text_gchat_timestamp_other);
            placeimage = itemView.findViewById(R.id.place_image_suggest);
            startvote = itemView.findViewById(R.id.start_vote);
            yes = itemView.findViewById(R.id.vote_yes);
            no = itemView.findViewById(R.id.vote_no);
            voted = itemView.findViewById(R.id.voted);
            user_pic = itemView.findViewById(R.id.image_gchat_profile);
            placeaddress = itemView.findViewById(R.id.text_gchat_desc_other);

        }
    }

    public static class OutGoingSuggestedViewHolder extends RecyclerView.ViewHolder {
        TextView placename, time, placeaddress;
        ImageView placeimage;
        MaterialCardView startvote, yes, no, voted;

        public OutGoingSuggestedViewHolder(@NonNull View itemView) {
            super(itemView);
            placename = itemView.findViewById(R.id.text_gchat_message_other);
            time = itemView.findViewById(R.id.text_gchat_timestamp_other);
            placeimage = itemView.findViewById(R.id.place_image_suggest);
            startvote = itemView.findViewById(R.id.start_vote);
            yes = itemView.findViewById(R.id.vote_yes);
            no = itemView.findViewById(R.id.vote_no);
            voted = itemView.findViewById(R.id.voted);
            placeaddress = itemView.findViewById(R.id.text_gchat_desc_other);

        }
    }
}
