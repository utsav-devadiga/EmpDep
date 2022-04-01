package com.pro.empdep.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FriendList implements Parcelable {
    ArrayList<String> friends;


    public FriendList(ArrayList<String> friends) {
        this.friends = friends;
    }

    protected FriendList(Parcel in) {
        friends = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(friends);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FriendList> CREATOR = new Creator<FriendList>() {
        @Override
        public FriendList createFromParcel(Parcel in) {
            return new FriendList(in);
        }

        @Override
        public FriendList[] newArray(int size) {
            return new FriendList[size];
        }
    };

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "FriendList{" +
                "friends=" + friends +
                '}';
    }
}
