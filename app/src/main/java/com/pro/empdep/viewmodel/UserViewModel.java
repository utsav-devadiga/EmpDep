package com.pro.empdep.viewmodel;


import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.model.User;
import com.pro.empdep.repo.UserRepository;
import com.pro.empdep.screens.HomeFragmentDirections;

public class UserViewModel extends ViewModel {

    public MutableLiveData<User> mUser;
    FirebaseFirestore db;
    UserRepository userRepository;
    public MutableLiveData<Boolean> isLoading;
    public static String TAG = "USER-VIEW-MODEL";
    FirebaseAuth mAuth;

    public UserViewModel() {
        this.mUser = new MutableLiveData<>();
        this.db = FirebaseFirestore.getInstance();
        userRepository = new UserRepository();
        isLoading = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * @function saveUserInFirebase() method will call the
     * repo's set method
     */
    public void saveUserInFirebase(User user) {
        isLoading.setValue(true);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        CollectionReference userGroup = db.collection(Credentials.USER);
        user.setPhoneNumber(firebaseUser.getPhoneNumber());
        userGroup.document(user.getId()).set(user).addOnSuccessListener(unused -> {
            Log.d(TAG, "Firebase: User Saved");
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(user.getUserName())
                    .setPhotoUri(Uri.parse(user.getPhotoUrl()))
                    .build();
            firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    isLoading.setValue(false);
                    Log.d(TAG, "User profile updated.");
                }
            });

        }).addOnFailureListener(e -> {
            isLoading.setValue(true);
            Log.d(TAG, "Firebase: Failed to save User" + e.getLocalizedMessage());
        });

    }

    public void getUserFromFirebase() {
        try {
            isLoading.setValue(true);
            FirebaseUser currentUser = mAuth.getCurrentUser();
            User user = new User();
            user.setUserName(currentUser.getDisplayName());
            user.setPhoneNumber(currentUser.getPhoneNumber());
            user.setPhotoUrl(currentUser.getPhotoUrl().toString());
            user.setGender("MALE");
            user.setId(currentUser.getUid());
            isLoading.setValue(false);
            mUser.postValue(user);
        } catch (Exception e) {
            isLoading.setValue(false);
        }
    }


    public void checkUserExist(View view) {
        CollectionReference userGroup = db.collection(Credentials.USER);

        userGroup.document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    //user does not exist
                    NavDirections actions = HomeFragmentDirections.actionHomeFragmentToUserInfoFragment(mAuth.getCurrentUser().getPhoneNumber());
                    Navigation.findNavController(view).navigate(actions);
                }
            } else {
                Toast.makeText(view.getContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }

        });


    }
}
