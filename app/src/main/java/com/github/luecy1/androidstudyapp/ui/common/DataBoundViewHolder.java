package com.github.luecy1.androidstudyapp.ui.common;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by you on 2018/01/29.
 */

public class DataBoundViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public final T binding;
    DataBoundViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
