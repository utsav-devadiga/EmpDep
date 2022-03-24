package com.pro.empdep.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.pro.empdep.firebase.RandomPhotoUrlGenerator;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    String phoneNumber;
    String userName;
    String id;
    String gender;
    String photoUrl;
    List<String> friends;
    List<String> friendReq;
    String device;
    List<String> groups;
    List<String> groupReq;


    public User() {
    }

    public User(String phoneNumber, String userName, String id, String gender, String photoUrl,String device) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.id = id;
        this.gender = gender;
        this.photoUrl = photoUrl;
        friends = new ArrayList<>();
        friendReq = new ArrayList<>();
        groups = new ArrayList<>();
        groupReq = new ArrayList<>();
        this.device = device;
    }

    protected User(Parcel in) {
        phoneNumber = in.readString();
        userName = in.readString();
        id = in.readString();
        gender = in.readString();
        photoUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getGroupReq() {
        return groupReq;
    }

    public void setGroupReq(List<String> groupReq) {
        this.groupReq = groupReq;
    }

    public List<String> getFriendReq() {
        return friendReq;
    }

    public void setFriendReq(List<String> friendReq) {
        this.friendReq = friendReq;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phoneNumber);
        parcel.writeString(userName);
        parcel.writeString(id);
        parcel.writeString(gender);
        parcel.writeString(photoUrl);
    }

    @Override
    public String toString() {
        return "User{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", id='" + id + '\'' +
                ", gender='" + gender + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
