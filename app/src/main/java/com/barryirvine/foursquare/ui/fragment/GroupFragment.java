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
import com.barryirvine.foursquare.model.Item;
import com.barryirvine.foursquare.ui.adapter.GroupAdapter;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private GroupAdapter mAdapter;
    private ArrayList<Item> mItems;

    public GroupFragment() {
    }

    public static GroupFragment newInstance(@NonNull final ArrayList<Item> items) {
        final GroupFragment fragment = new GroupFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(Args.ITEMS, items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems = getArguments().getParcelableArrayList(Args.ITEMS);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (savedInstanceState == null) {
            mAdapter = new GroupAdapter(mItems);
            mRecyclerView.setAdapter(mAdapter);
        }
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private static class Args {
        private static final String ITEMS = "ITEMS";
    }
}
