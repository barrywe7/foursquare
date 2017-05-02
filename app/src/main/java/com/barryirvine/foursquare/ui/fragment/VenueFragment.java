package com.barryirvine.foursquare.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barryirvine.foursquare.R;
import com.barryirvine.foursquare.model.Venue;
import com.barryirvine.foursquare.ui.adapter.VenueAdapter;

import java.util.ArrayList;

public class VenueFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private VenueAdapter mAdapter;
    private ArrayList<Venue> mVenues;

    public VenueFragment() {
    }

    public static VenueFragment newInstance(@NonNull final ArrayList<Venue> venues) {
        final VenueFragment fragment = new VenueFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(Args.VENUES, venues);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVenues = getArguments().getParcelableArrayList(Args.VENUES);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_venue, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (savedInstanceState == null) {
            mAdapter = new VenueAdapter(mVenues);
            mRecyclerView.setAdapter(mAdapter);
        }
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private static class Args {
        private static final String VENUES = "VENUES";
    }
}
