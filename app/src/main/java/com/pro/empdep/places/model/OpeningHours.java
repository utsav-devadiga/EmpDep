package com.pro.empdep.places.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours implements Parcelable {
    @SerializedName("open_now")
    @Expose
    public Boolean openNow;

    protected OpeningHours(Parcel in) {
        byte tmpOpenNow = in.readByte();
        openNow = tmpOpenNow == 0 ? null : tmpOpenNow == 1;
    }

    public static final Creator<OpeningHours> CREATOR = new Creator<OpeningHours>() {
        @Override
        public OpeningHours createFromParcel(Parcel in) {
            return new OpeningHours(in);
        }

        @Override
        public OpeningHours[] newArray(int size) {
            return new OpeningHours[size];
        }
    };

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (openNow == null ? 0 : openNow ? 1 : 2));
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
                "openNow=" + openNow +
                '}';
    }
}
