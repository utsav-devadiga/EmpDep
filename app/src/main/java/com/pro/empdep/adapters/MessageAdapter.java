package com.pro.empdep.adapters;

import android.content.Context;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.R;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.Message;
import com.pro.empdep.model.User;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Message> messageList;
    Context context;
    FirebaseAuth mAuth;
    String current_user_id = "";
    FirebaseFirestore db;

    public MessageAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
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
        if (current_user_id.equals(messageList.get(position).getSent_by())) {
            return 1;
        } else if (position != 0 && messageList.get(position).getSent_by().equals(messageList.get(position - 1).getSent_by())) {
            return 2;
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
}
