package com.pro.empdep.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pro.empdep.firebase.Credentials;
import com.pro.empdep.firebase.RandomPhotoUrlGenerator;
import com.pro.empdep.model.Group;

import java.util.ArrayList;

public class GroupRepository {


    FirebaseFirestore db;
    public static String TAG = "USER-REPO";
    FirebaseAuth auth;


    public GroupRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }


    public LiveData<Boolean> groupCreate(ArrayList<String> groupMembers,String groupName) {
        final MutableLiveData<Boolean> GROUP_STATUS = new MutableLiveData<>();
        GROUP_STATUS.setValue(true);
        String currentUserId = auth.getCurrentUser().getUid();
        String groupId = db.collection(Credentials.GROUP).document().getId();
        RandomPhotoUrlGenerator randomPhotoUrlGenerator = new RandomPhotoUrlGenerator();
        String groupPic = randomPhotoUrlGenerator.randomUsernameExtension();

        //Todo Complete the group name String by getting the name from other fragment
        Group group = new Group(groupId,0,currentUserId,"NO-MESSAGES","NO-MESSAGES",new ArrayList<String>(),new ArrayList<String>(),groupMembers,groupMembers,String.valueOf(System.currentTimeMillis()),groupPic,groupName,currentUserId);
        db.collection(Credentials.GROUP);

        return GROUP_STATUS;
    }

}

