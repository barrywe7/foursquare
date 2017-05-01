package com.barryirvine.foursquare.model;

import com.google.gson.annotations.SerializedName;

public class SearchResponse {

    private Meta meta;
    @SerializedName("response")
    private VenueResponse mVenues;
}
