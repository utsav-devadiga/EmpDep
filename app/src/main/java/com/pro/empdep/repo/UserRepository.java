package com.pro.empdep.repo;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.User;

public class UserRepository {

    MutableLiveData<User> mUser;
    FirebaseFirestore db;
    public static String TAG = "USER-REPO";

    public UserRepository() {
        this.mUser = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
    }





}
