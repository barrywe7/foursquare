package com.barryirvine.foursquare.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.barryirvine.foursquare.R;
import com.barryirvine.foursquare.model.Item;


/**
 * View Model for {@link Item} Remember to use the {@link Bindable} annotation for all getters and to do
 * {@link #notifyPropertyChanged(int)} after a value is set in a setter.
 */

public class ItemViewModel extends BaseObservable {

    private final Item mItem;
    private final Context mContext;

    public ItemViewModel(@NonNull final Context context, final Item item) {
        mContext = context;
        mItem = item;
    }

    @Bindable
    public String getName() {
        return mItem.getVenue().getName();
    }

    @Bindable
    public String getDistance() {
        return mContext.getString(R.string.f_distance_km, mItem.getVenue().getLocation().getDistance() / 1000f);
    }

    @Bindable
    public String getAddress() {
        return mItem.getVenue().getLocation().getAddress();
    }

    @Bindable
    public String getTown() {
        final String city = mItem.getVenue().getLocation().getCity();
        if (city != null && city.length() > 0) {
            return city + ", " + mItem.getVenue().getLocation().getState();
        } else {
            return mItem.getVenue().getLocation().getState();
        }
    }


    public void onClick() {
        //TODO
    }
}
