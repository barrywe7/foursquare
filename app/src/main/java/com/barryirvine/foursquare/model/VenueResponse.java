package com.barryirvine.foursquare.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VenueResponse {

    private Meta meta;
    @SerializedName("venues")
    private List<Venue> mVenues;
}
