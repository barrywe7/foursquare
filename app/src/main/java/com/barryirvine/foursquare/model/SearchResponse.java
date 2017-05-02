package com.barryirvine.foursquare.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchResponse {

    private Meta meta;
    @SerializedName("response")
    private VenueResponse mVenues;

    public ArrayList<Venue> getVenues() {
        return new ArrayList<>(mVenues.getVenues());
    }
}
