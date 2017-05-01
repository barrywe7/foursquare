package com.barryirvine.foursquare.model;

import com.google.gson.annotations.SerializedName;

public class ExploreResponse {
    private Meta meta;
    @SerializedName("response")
    private ExploreDetailResponse mResponse;

}
