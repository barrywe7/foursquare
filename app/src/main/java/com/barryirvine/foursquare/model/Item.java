package com.barryirvine.foursquare.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    private Venue venue;

    public Item() {
    }

    protected Item(Parcel in) {
        this.venue = in.readParcelable(Venue.class.getClassLoader());
    }

    public Venue getVenue() {
        return venue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.venue, flags);
    }
}
