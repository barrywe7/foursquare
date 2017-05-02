package com.barryirvine.foursquare.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.barryirvine.foursquare.R;
import com.barryirvine.foursquare.model.Venue;
import com.barryirvine.foursquare.ui.activity.RecommendedVenuesActivity;


/**
 * View Model for {@link Venue} Remember to use the {@link Bindable} annotation for all getters and to do
 * {@link #notifyPropertyChanged(int)} after a value is set in a setter.
 */

public class VenueViewModel extends BaseObservable {

    private final Venue mVenue;
    private final Context mContext;

    public VenueViewModel(@NonNull final Context context, final Venue venue) {
        mContext = context;
        mVenue = venue;
    }

    @Bindable
    public String getName() {
        return mVenue.getName();
    }

    @Bindable
    public String getDistance() {
        return mContext.getString(R.string.f_distance_km, mVenue.getLocation().getDistance() / 1000f);
    }

    @Bindable
    public String getAddress() {
        return mVenue.getLocation().getAddress();
    }

    @Bindable
    public String getTown() {
        final String city = mVenue.getLocation().getCity();
        if (city != null && city.length() > 0) {
            return city + ", " + mVenue.getLocation().getState();
        } else {
            return mVenue.getLocation().getState();
        }
    }


    public void onClick() {
        RecommendedVenuesActivity.start(mContext, mVenue.getLocation().getLat(), mVenue.getLocation().getLng());
    }
}
