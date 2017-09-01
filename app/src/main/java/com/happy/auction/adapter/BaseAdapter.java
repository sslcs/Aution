package com.happy.auction.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-8-18.
 * Adapter基础类
 */

public class BaseAdapter<T> extends RecyclerView.Adapter {
    private OnItemClickListener onItemClickListener;
    private ArrayList<T> dataList;

    public BaseAdapter() {
        this(null);
    }

    public BaseAdapter(ArrayList<T> data) {
        dataList = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (onItemClickListener == null) return;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void addItem(T item) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.add(item);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<T> data) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        if (dataList == null) return;
        dataList.clear();
        notifyDataSetChanged();
    }

    public boolean removeItem(T item) {
        if (dataList == null) return false;
        boolean success = dataList.remove(item);
        notifyDataSetChanged();
        return success;
    }

    public T getItem(int position) {
        if (dataList == null || position < 0 || position >= dataList.size()) {
            return null;
        }
        return dataList.get(position);
    }

    public T removeItem(int position) {
        if (dataList == null || position < 0 || position >= dataList.size()) {
            return null;
        }
        T item = dataList.remove(position);
        notifyDataSetChanged();
        return item;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        onItemClickListener = l;
    }

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
