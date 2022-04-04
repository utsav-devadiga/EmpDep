package com.pro.empdep.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.Group;
import com.pro.empdep.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageRepository {
    FirebaseAuth mAuth;
    FirebaseFirestore db;


    public MessageRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public LiveData<List<Message>> getMessages(String groupId) {

        final MutableLiveData<List<Message>> messageList = new MutableLiveData<>();
        db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).orderBy("timestamp").addSnapshotListener((value, error) -> {
            if (error == null) {
                if (!value.getDocuments().isEmpty()) {

                    messageList.postValue(value.toObjects(Message.class));
                } else {
                    //no messages
                    messageList.setValue(null);
                }


            } else {
                messageList.setValue(null);
            }
        });

        return messageList;
    }

    public LiveData<Boolean> sendMessageNormal(String groupId, String message) {
        final MutableLiveData<Boolean> messageSent = new MutableLiveData<>();
        String currentUserId = mAuth.getCurrentUser().getUid();
        String message_id = db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document().getId();
        ArrayList<String> messageSeenBy = new ArrayList<>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        messageSeenBy.add(currentUserId);
        Message messageToSend = new Message(message, Credentials.MESSAGES_NORMAL, currentUserId, messageSeenBy, timestamp, message_id, new ArrayList<String>(), new ArrayList<String>(), 0, 0, 0);
        db.collection(Credentials.GROUP).document(groupId).collection(Credentials.MESSAGES).document(message_id).set(messageToSend).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageSent.setValue(true);
                db.collection(Credentials.GROUP).document(groupId).update("last_message", message);
                db.collection(Credentials.GROUP).document(groupId).update("last_message_sent_by", currentUserId);
                db.collection(Credentials.GROUP).document(groupId).update("timestamp", timestamp);
                db.collection(Credentials.GROUP).document(groupId).update("seen_by", messageSeenBy);
            } else {
                messageSent.setValue(false);
            }
        });
        return messageSent;
    }
}
