package com.barryirvine.foursquare.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExploreResponse {
    private Meta meta;
    @SerializedName("response")
    private ExploreDetailResponse mResponse;

    public ArrayList<Item> getItems() {
        return new ArrayList<>(mResponse.getItems());
    }
}
