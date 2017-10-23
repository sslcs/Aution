package com.happy.auction.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter基类<br/>
 * Created by LiuCongshan on 17-09-23.
 *
 * @author LiuCongshan
 */
public abstract class BaseAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<CustomViewHolder<B>> {
    private List<T> data;
    private OnItemClickListener<T> onListener;

    public BaseAdapter() {}

    public BaseAdapter(List<T> items) {
        addAll(items);
    }

    public void clear() {
        if (data == null) {
            return;
        }
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<T> items) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(items);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        if (data == null || position >= data.size() || position < 0) {
            return null;
        }
        return data.get(position);
    }

    public int getPosition(T item) {
        if (data == null || data.isEmpty()) {
            return -1;
        }
        return data.indexOf(item);
    }

    public T getLast() {
        if (data == null) {
            return null;
        }
        return getItem(data.size() - 1);
    }

    @Override
    public CustomViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CustomViewHolder<>(getBinding(parent, inflater));
    }

    public abstract B getBinding(ViewGroup parent, LayoutInflater inflater);

    @Override
    public void onBindViewHolder(final CustomViewHolder<B> holder, final int position) {
        final T item = getItem(position);
        if (item != null) {
            bindItem(holder.getBinding(), item, position);

            if (onListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onListener.onItemClick(view, item, holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    public abstract void bindItem(B binding, T item, int position);

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onListener = listener;
    }

    public void addItem(T item) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(item);
        notifyItemInserted(data.size() - 1);
    }

    public void addItem(int position, T item) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(position, item);
        notifyItemInserted(position);
    }

    public T removeItem(int position) {
        if (data == null || position < 0 || position >= data.size()) {
            return null;
        }
        T item = data.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    final public int getRealCount() {
        return data == null ? 0 : data.size();
    }

    final public boolean isEmpty() {
        return data == null || data.isEmpty();
    }
}
