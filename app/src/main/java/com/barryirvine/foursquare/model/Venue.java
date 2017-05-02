package com.barryirvine.foursquare.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Venue implements Parcelable {

    public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel source) {
            return new Venue(source);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };
    private String id;
    private String name;
    private String url;
    private FourSquareLocation location;

    public Venue() {
    }

    protected Venue(final Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.url = in.readString();
        this.location = in.readParcelable(FourSquareLocation.class.getClassLoader());
    }

    public String getName() {
        return name;
    }

    public FourSquareLocation getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeParcelable(this.location, flags);
    }
}
