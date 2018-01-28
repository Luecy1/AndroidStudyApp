package com.github.luecy1.androidstudyapp.ui.common;

import android.annotation.SuppressLint;
import android.databinding.ViewDataBinding;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by you on 2018/01/29.
 */

public abstract class DataBoundListAdapter<T, V extends ViewDataBinding>
        extends RecyclerView.Adapter<DataBoundViewHolder<V>> {

    @Nullable
    private List<T> items;

    private int dataVersion = 0;


    @Override
    public final DataBoundViewHolder<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        V binding = createBinding(parent);
        return new DataBoundViewHolder<>(binding);
    }

    protected abstract V createBinding(ViewGroup parent);

    @Override
    public void onBindViewHolder(DataBoundViewHolder<V> holder, int position) {
        bind(holder.binding, items.get(position));
        holder.binding.executePendingBindings();
    }

    protected abstract void bind(V binding, T item);

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replace(List<T> update) {
        dataVersion++;
        if (items == null) {
            if (update == null) {
                return;
            }
            items = update;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
