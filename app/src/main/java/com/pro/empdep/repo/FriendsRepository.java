package com.pro.empdep.repo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.User;
import com.pro.empdep.places.response.PlacesResponse;

import java.util.List;

public class FriendsRepository {

    FirebaseFirestore db;
    public static String TAG = "USER-REPO";

    public FriendsRepository() {

        db = FirebaseFirestore.getInstance();
    }

    public LiveData<List<User>> getAllUsersFromServer() {
        final MutableLiveData<List<User>> allUsers = new MutableLiveData<>();
        db.collection(Credentials.USER).addSnapshotListener((value, error) -> {
            if (error != null) {
                allUsers.setValue(null);
            } else {
                if (value != null) {
                    allUsers.setValue(value.toObjects(User.class));
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        return allUsers;
    }


    public User getSingleUser(String id) {
        final User[] user = {null};
        db.collection(Credentials.USER).document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                user[0] = task.getResult().toObject(User.class);
            }else{

            }
        });


        return user[0];
    }

}
 /* if (task.isSuccessful()) {
          allUsers.setValue(task.getResult().toObjects(User.class));
        } else {
        allUsers.setValue(null);
        }*/