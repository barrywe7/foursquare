package com.barryirvine.foursquare.api;


import com.barryirvine.foursquare.model.VenueResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareAPI {

    String BASE_URL = "https://api.foursquare.com/";
    String VERSION = "20161016";

    @GET("v2/venues/search")
    Observable<VenueResponse> findVenues(@Query("v") final String verifiedDate, @Query("ll") final String latLng, @Query("query") final String query, @Query("client_id") final String clientId, @Query("client_secret") final String clientSecret);

}
