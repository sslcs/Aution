package com.happy.auction.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.happy.auction.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter基类
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.CustomViewHolder> {
    private List<T> data;
    private OnItemClickListener onItemClickListener;

    public BaseAdapter() {}

    public BaseAdapter(List<T> items) {
        addAll(items);
    }

    public void clear() {
        if(data==null) return;
        data.clear();
    }

    public void addAll(List<T> items) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(items);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        if (data == null || position >= data.size()) return null;
        return data.get(position);
    }

    public int getPosition(T item) {
        if (data == null || data.isEmpty()) return -1;
        return data.indexOf(item);
    }

    public void refresh(int position, T item) {
        if (data == null || data.isEmpty() || position >= data.size()) return;
        data.remove(position);
        data.add(position, item);
    }

    @Override
    public void onBindViewHolder(final BaseAdapter.CustomViewHolder holder, int position) {
        if (onItemClickListener == null) return;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void addItem(T item) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(item);
        notifyDataSetChanged();
    }

    public void addItem(int position, T item) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(position, item);
        notifyDataSetChanged();
    }

    public T removeItem(int position) {
        if (data == null || position < 0 || position >= data.size()) {
            return null;
        }
        T item = data.remove(position);
        notifyDataSetChanged();
        return item;
    }

    public boolean removeItem(T item) {
        if (data == null) return false;
        boolean success = data.remove(item);
        notifyDataSetChanged();
        return success;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class CustomViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public final B binding;

        public CustomViewHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public B getBinding() {
            return binding;
        }
    }
}
