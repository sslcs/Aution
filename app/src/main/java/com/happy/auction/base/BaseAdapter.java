package com.happy.auction.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
                T item = getItem(holder.getAdapterPosition());
                onItemClickListener.onItemClick(view, holder.getAdapterPosition(), item);
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Object item);
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
