package com.barryirvine.foursquare.ui.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.barryirvine.foursquare.BR;
import com.barryirvine.foursquare.R;
import com.barryirvine.foursquare.model.Item;
import com.barryirvine.foursquare.viewmodel.ItemViewModel;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private final List<Item> mItems;

    public GroupAdapter(final List<Item> items) {
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, @LayoutRes final int viewType) {
        return new GroupAdapter.ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false));
    }

    @LayoutRes
    @Override
    public int getItemViewType(final int position) {
        return R.layout.item_group;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(new ItemViewModel(holder.itemView.getContext(), mItems.get(position)));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding mBinding;

        ViewHolder(@NonNull final ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(@NonNull final ItemViewModel itemViewModel) {
            mBinding.setVariable(BR.viewModel, itemViewModel);
            mBinding.executePendingBindings();
        }
    }
}
