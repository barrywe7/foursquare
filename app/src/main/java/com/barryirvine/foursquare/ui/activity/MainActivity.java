package com.barryirvine.foursquare.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Toast;

import com.barryirvine.foursquare.BuildConfig;
import com.barryirvine.foursquare.R;
import com.barryirvine.foursquare.api.FourSquareService;
import com.barryirvine.foursquare.api.FoursquareAPI;
import com.barryirvine.foursquare.model.ExploreResponse;
import com.barryirvine.foursquare.model.SearchResponse;
import com.barryirvine.foursquare.ui.fragment.VenueFragment;
import com.barryirvine.foursquare.utils.RuntimePermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, ResultCallback<LocationSettingsResult>, SearchView.OnQueryTextListener {

    private static final int REQUEST_LOCATION_SETTING = 1234;
    private Location mLocation;
    private GoogleApiClient mLocationClient;
    private LocationRequest mLocationRequest;
    private SearchView mSearchView;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mSearchView.onActionViewExpanded();
            }
        });
        mSearchView.setOnQueryTextListener(this);
        handleLocationPermission();
    }

    private void handleLocationPermission() {
        RuntimePermissionUtils.checkForPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                new RuntimePermissionUtils.CheckPermissionListener() {
                    @Override
                    public void onPermissionAlreadyGranted() {
                        checkLocationSettings(MainActivity.this);
                    }

                    @Override
                    public void onPermissionAlreadyDeniedWithDoNotAskAgain() {
                        RuntimePermissionUtils.missingPermissionAlertDialog(MainActivity.this, R.string.missing_location_permission_title, R.string.missing_location_permission_body, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(final DialogInterface dialog) {
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onPermissionPendingRequest() {
                        RuntimePermissionUtils.requestPermissionFromActivity(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, RuntimePermissionUtils.REQUEST_LOCATION);
                    }
                });
    }

    @Override
    public void onResult(@NonNull final LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // Noop - will happen elsewhere
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    disconnectLocationClient();
                    mLocationClient = null;
                    status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION_SETTING);
                } catch (final IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                break;
        }
    }

    private void disconnectLocationClient() {
        if (mLocationClient != null && mLocationClient.isConnected()) {
            mLocationClient.disconnect();
        }
    }

    private void connectLocationClient() {
        if (!mLocationClient.isConnecting()) {
            mLocationClient.connect();
        }
    }

    private void setLocationClient() {
        if (mLocationClient == null) {
            mLocationClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
            mLocationRequest = new LocationRequest()
                    .setInterval(10 * DateUtils.SECOND_IN_MILLIS)
                    .setFastestInterval(5 * DateUtils.SECOND_IN_MILLIS)
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
    }

    private void checkLocationSettings(final ResultCallback<LocationSettingsResult> callback) {
        if (mLocation == null) {
            setLocationClient();
            final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mLocationClient,
                    new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest).build());
            result.setResultCallback(callback);
            connectLocationClient();
        }
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected(@Nullable final Bundle bundle) {
        if (LocationServices.FusedLocationApi.getLastLocation(mLocationClient) != null) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
            if (RuntimePermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) { // && getSupportFragmentManager().findFragmentById(R.id.fragment_layout) == null) {
                getRecommended();
            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, this);
                    mLocation = location;
                    if (RuntimePermissionUtils.hasPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION))  {// && getSupportFragmentManager().findFragmentById(R.id.fragment_layout) == null) {
                        getRecommended();
                    }
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(final int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }

    private void getVenues(final String query) {
        FourSquareService.get().findVenues(FoursquareAPI.VERSION,
                String.valueOf(mLocation.getLatitude()) + "," + String.valueOf(mLocation.getLongitude()),
                query,
                BuildConfig.FOUR_SQUARE_CLIENT_ID,
                BuildConfig.FOUR_SQUARE_SECRET)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<SearchResponse>() {
                            @Override
                            public void accept(final SearchResponse response) throws Exception {
                                addFragment(R.id.fragment_layout, VenueFragment.newInstance(response.getVenues()));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(final Throwable throwable) throws Exception {
                                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_LONG).show();
                            }
                        });
    }

    private void getRecommended() {
        FourSquareService.get().recommended(FoursquareAPI.VERSION,
                "51.567780042666996,0.010843923594280032",
                BuildConfig.FOUR_SQUARE_CLIENT_ID,
                BuildConfig.FOUR_SQUARE_SECRET)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ExploreResponse>() {
                            @Override
                            public void accept(final ExploreResponse response) throws Exception {
                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(final Throwable throwable) throws Exception {
                                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_LONG).show();
                            }
                        });


    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        if (mLocation == null) {
            Snackbar.make(mCoordinatorLayout, R.string.no_location, Snackbar.LENGTH_LONG).show();
        } else {
            try {
                getVenues(URLEncoder.encode(query, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        return false;
    }

    public void addFragment(@IdRes final int containerViewId, final Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment, fragment.getClass().getSimpleName()).commitNow();
    }
}
