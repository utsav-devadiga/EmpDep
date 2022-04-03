package com.pro.empdep.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Group implements Parcelable {

    String group_id;
    int group_type;
    String created_by;
    String last_message;
    String last_message_sent_by;
    ArrayList<String> seen_by;
    ArrayList<String> users;
    ArrayList<String> pendingRequest;
    ArrayList<String> pendingSentRequest;
    String timestamp;
    String group_picture;
    String group_name;
    String group_admin;

    public Group() {
    }

    public Group(String group_id, int group_type, String created_by, String last_message, String last_message_sent_by, ArrayList<String> seen_by, ArrayList<String> users, ArrayList<String> pendingRequest, ArrayList<String> pendingSentRequest, String timestamp, String group_picture, String group_name, String group_admin) {
        this.group_id = group_id;
        this.group_type = group_type;
        this.created_by = created_by;
        this.last_message = last_message;
        this.last_message_sent_by = last_message_sent_by;
        this.seen_by = seen_by;
        this.users = users;
        this.pendingRequest = pendingRequest;
        this.pendingSentRequest = pendingSentRequest;
        this.timestamp = timestamp;
        this.group_picture = group_picture;
        this.group_name = group_name;
        this.group_admin = group_admin;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public int getGroup_type() {
        return group_type;
    }

    public void setGroup_type(int group_type) {
        this.group_type = group_type;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getLast_message_sent_by() {
        return last_message_sent_by;
    }

    public void setLast_message_sent_by(String last_message_sent_by) {
        this.last_message_sent_by = last_message_sent_by;
    }

    public ArrayList<String> getSeen_by() {
        return seen_by;
    }

    public void setSeen_by(ArrayList<String> seen_by) {
        this.seen_by = seen_by;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public ArrayList<String> getPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(ArrayList<String> pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public ArrayList<String> getPendingSentRequest() {
        return pendingSentRequest;
    }

    public void setPendingSentRequest(ArrayList<String> pendingSentRequest) {
        this.pendingSentRequest = pendingSentRequest;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getGroup_picture() {
        return group_picture;
    }

    public void setGroup_picture(String group_picture) {
        this.group_picture = group_picture;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_admin() {
        return group_admin;
    }

    public void setGroup_admin(String group_admin) {
        this.group_admin = group_admin;
    }

    protected Group(Parcel in) {
        group_id = in.readString();
        group_type = in.readInt();
        created_by = in.readString();
        last_message = in.readString();
        last_message_sent_by = in.readString();
        seen_by = in.createStringArrayList();
        users = in.createStringArrayList();
        pendingRequest = in.createStringArrayList();
        pendingSentRequest = in.createStringArrayList();
        timestamp = in.readString();
        group_picture = in.readString();
        group_name = in.readString();
        group_admin = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(group_id);
        parcel.writeInt(group_type);
        parcel.writeString(created_by);
        parcel.writeString(last_message);
        parcel.writeString(last_message_sent_by);
        parcel.writeStringList(seen_by);
        parcel.writeStringList(users);
        parcel.writeStringList(pendingRequest);
        parcel.writeStringList(pendingSentRequest);
        parcel.writeString(timestamp);
        parcel.writeString(group_picture);
        parcel.writeString(group_name);
        parcel.writeString(group_admin);
    }

    @Override
    public String toString() {
        return "Group{" +
                "group_id='" + group_id + '\'' +
                ", group_type=" + group_type +
                ", created_by='" + created_by + '\'' +
                ", last_message='" + last_message + '\'' +
                ", last_message_sent_by='" + last_message_sent_by + '\'' +
                ", seen_by=" + seen_by +
                ", users=" + users +
                ", pendingRequest=" + pendingRequest +
                ", pendingSentRequest=" + pendingSentRequest +
                ", timestamp='" + timestamp + '\'' +
                ", group_picture='" + group_picture + '\'' +
                ", group_name='" + group_name + '\'' +
                ", group_admin='" + group_admin + '\'' +
                '}';
    }
}
