package com.barryirvine.foursquare.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.barryirvine.foursquare.BuildConfig;
import com.barryirvine.foursquare.R;
import com.barryirvine.foursquare.api.FourSquareService;
import com.barryirvine.foursquare.api.FoursquareAPI;
import com.barryirvine.foursquare.model.ExploreResponse;
import com.barryirvine.foursquare.ui.fragment.GroupFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RecommendedVenuesActivity extends AppCompatActivity {

    private double mLat;
    private double mLng;

    public static void start(@NonNull final Context context, final double latitude, final double longitude) {
        context.startActivity(makeIntent(context, latitude, longitude));
    }

    public static Intent makeIntent(@NonNull final Context context, final double latitude, final double longitude) {
        return new Intent(context, RecommendedVenuesActivity.class)
                .putExtra(Extras.LATITUDE, latitude)
                .putExtra(Extras.LONGITUDE, longitude);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_venues);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mLat = getIntent().getDoubleExtra(Extras.LATITUDE, 0);
        mLng = getIntent().getDoubleExtra(Extras.LONGITUDE, 0);
        getRecommended();
    }

    private void getRecommended() {
        FourSquareService.get().recommended(FoursquareAPI.VERSION,
                mLat + "," + mLng,
                BuildConfig.FOUR_SQUARE_CLIENT_ID,
                BuildConfig.FOUR_SQUARE_SECRET)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ExploreResponse>() {
                            @Override
                            public void accept(final ExploreResponse response) throws Exception {
                                addFragment(R.id.fragment_layout, GroupFragment.newInstance(response.getItems()));
                                Toast.makeText(RecommendedVenuesActivity.this, "Success", Toast.LENGTH_LONG).show();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(final Throwable throwable) throws Exception {
                                Toast.makeText(RecommendedVenuesActivity.this, "Failure", Toast.LENGTH_LONG).show();
                            }
                        });


    }

    public void addFragment(@IdRes final int containerViewId, final Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment, fragment.getClass().getSimpleName()).commitNow();
    }

    static class Extras {
        static final String LATITUDE = "LATITUDE";
        static final String LONGITUDE = "LONGITUDE";

    }

}
