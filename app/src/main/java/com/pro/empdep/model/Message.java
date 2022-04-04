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
    ArrayList<String> reactions;
    ArrayList<String> voting_id;
    int vote_yes;
    int vote_no;
    int total_votes;

    public Message() {
    }

    public Message(String message, int message_type, String sent_by, ArrayList<String> seen_by, String timestamp, String message_id, ArrayList<String> reactions, ArrayList<String> voting_id, int vote_yes, int vote_no, int total_votes) {
        this.message = message;
        this.message_type = message_type;
        this.sent_by = sent_by;
        this.seen_by = seen_by;
        this.timestamp = timestamp;
        this.message_id = message_id;
        this.reactions = reactions;
        this.voting_id = voting_id;
        this.vote_yes = vote_yes;
        this.vote_no = vote_no;
        this.total_votes = total_votes;
    }


    protected Message(Parcel in) {
        message = in.readString();
        message_type = in.readInt();
        sent_by = in.readString();
        seen_by = in.createStringArrayList();
        timestamp = in.readString();
        message_id = in.readString();
        reactions = in.createStringArrayList();
        voting_id = in.createStringArrayList();
        vote_yes = in.readInt();
        vote_no = in.readInt();
        total_votes = in.readInt();
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

    public ArrayList<String> getReactions() {
        return reactions;
    }

    public void setReactions(ArrayList<String> reactions) {
        this.reactions = reactions;
    }

    public ArrayList<String> getVoting_id() {
        return voting_id;
    }

    public void setVoting_id(ArrayList<String> voting_id) {
        this.voting_id = voting_id;
    }

    public int getVote_yes() {
        return vote_yes;
    }

    public void setVote_yes(int vote_yes) {
        this.vote_yes = vote_yes;
    }

    public int getVote_no() {
        return vote_no;
    }

    public void setVote_no(int vote_no) {
        this.vote_no = vote_no;
    }

    public int getTotal_votes() {
        return total_votes;
    }

    public void setTotal_votes(int total_votes) {
        this.total_votes = total_votes;
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
        parcel.writeStringList(reactions);
        parcel.writeStringList(voting_id);
        parcel.writeInt(vote_yes);
        parcel.writeInt(vote_no);
        parcel.writeInt(total_votes);
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
                ", reactions=" + reactions +
                ", voting_id=" + voting_id +
                ", vote_yes=" + vote_yes +
                ", vote_no=" + vote_no +
                ", total_votes=" + total_votes +
                '}';
    }
}