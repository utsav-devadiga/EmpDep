package com.pro.empdep.places.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesModel implements Parcelable {

    @SerializedName("business_status")
    @Expose
    public String businessStatus;
    @SerializedName("formatted_address")
    @Expose
    public String formattedAddress;
    @SerializedName("geometry")
    @Expose
    public Geometry geometry;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("opening_hours")
    @Expose
    public OpeningHours openingHours;
    @SerializedName("photos")
    @Expose
    public List<Photo> photos = null;
    @SerializedName("place_id")
    @Expose
    public String placeId;
    @SerializedName("rating")
    @Expose
    public Double rating;
    @SerializedName("reference")
    @Expose
    public String reference;
    @SerializedName("types")
    @Expose
    public List<String> types = null;
    @SerializedName("user_ratings_total")
    @Expose
    public Integer userRatingsTotal;

    protected PlacesModel(Parcel in) {
        businessStatus = in.readString();
        formattedAddress = in.readString();
        name = in.readString();
        placeId = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        reference = in.readString();
        types = in.createStringArrayList();
        if (in.readByte() == 0) {
            userRatingsTotal = null;
        } else {
            userRatingsTotal = in.readInt();
        }
    }

    public static final Creator<PlacesModel> CREATOR = new Creator<PlacesModel>() {
        @Override
        public PlacesModel createFromParcel(Parcel in) {
            return new PlacesModel(in);
        }

        @Override
        public PlacesModel[] newArray(int size) {
            return new PlacesModel[size];
        }
    };

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(Integer userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(businessStatus);
        parcel.writeString(formattedAddress);
        parcel.writeString(name);
        parcel.writeString(placeId);
        if (rating == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(rating);
        }
        parcel.writeString(reference);
        parcel.writeStringList(types);
        if (userRatingsTotal == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(userRatingsTotal);
        }
    }

    @Override
    public String toString() {
        return "PlacesModel{" +
                "businessStatus='" + businessStatus + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", geometry=" + geometry +
                ", name='" + name + '\'' +
                ", openingHours=" + openingHours +
                ", photos=" + photos +
                ", placeId='" + placeId + '\'' +
                ", rating=" + rating +
                ", reference='" + reference + '\'' +
                ", types=" + types +
                ", userRatingsTotal=" + userRatingsTotal +
                '}';
    }
}
