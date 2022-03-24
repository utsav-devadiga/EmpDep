package com.pro.empdep.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Message implements Parcelable {
    String message;
    int message_type;
    String sent_by;
    ArrayList<String> seen_by;
    String timestamp;
    String message_id;

    public Message(String message, int message_type, String sent_by, ArrayList<String> seen_by, String timestamp, String message_id) {
        this.message = message;
        this.message_type = message_type;
        this.sent_by = sent_by;
        this.seen_by = seen_by;
        this.timestamp = timestamp;
        this.message_id = message_id;
    }

    protected Message(Parcel in) {
        message = in.readString();
        message_type = in.readInt();
        sent_by = in.readString();
        seen_by = in.createStringArrayList();
        timestamp = in.readString();
        message_id = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getSent_by() {
        return sent_by;
    }

    public void setSent_by(String sent_by) {
        this.sent_by = sent_by;
    }

    public ArrayList<String> getSeen_by() {
        return seen_by;
    }

    public void setSeen_by(ArrayList<String> seen_by) {
        this.seen_by = seen_by;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeInt(message_type);
        parcel.writeString(sent_by);
        parcel.writeStringList(seen_by);
        parcel.writeString(timestamp);
        parcel.writeString(message_id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", message_type=" + message_type +
                ", sent_by='" + sent_by + '\'' +
                ", seen_by=" + seen_by +
                ", timestamp='" + timestamp + '\'' +
                ", message_id='" + message_id + '\'' +
                '}';
    }
}
