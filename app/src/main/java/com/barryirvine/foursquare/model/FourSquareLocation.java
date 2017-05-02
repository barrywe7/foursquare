package com.barryirvine.foursquare.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FourSquareLocation implements Parcelable {
    public static final Parcelable.Creator<FourSquareLocation> CREATOR = new Parcelable.Creator<FourSquareLocation>() {
        @Override
        public FourSquareLocation createFromParcel(Parcel source) {
            return new FourSquareLocation(source);
        }

        @Override
        public FourSquareLocation[] newArray(int size) {
            return new FourSquareLocation[size];
        }
    };
    private String address;
    private double lat;
    private double lng;
    private int distance;
    private String city;
    private String state;

    public FourSquareLocation() {
    }

    protected FourSquareLocation(final Parcel in) {
        this.address = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.distance = in.readInt();
        this.city = in.readString();
        this.state = in.readString();
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getDistance() {
        return distance;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeInt(this.distance);
        dest.writeString(this.city);
        dest.writeString(this.state);
    }
}
