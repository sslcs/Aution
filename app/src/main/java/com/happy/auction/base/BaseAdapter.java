package com.happy.auction.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter基类
 */
public abstract class BaseAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private List<T> data;

    public BaseAdapter() {}

    public BaseAdapter(List<T> items) {
        addAll(items);
    }

    public void clear() {
        data.clear();
    }

    public void addAll(List<T> items) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(items);
    }

    public T getItem(int position) {
        if (data == null || position >= data.size()) return null;
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}
