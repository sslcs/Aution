package com.happy.auction.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter基类
 *
 * @author LiuCongshan
 * @date 17-09-23
 */
public abstract class BaseAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<CustomViewHolder<B>> {
    private List<T> mData;
    private OnItemClickListener<T> mListener;

    public BaseAdapter() {}

    public BaseAdapter(List<T> items) {
        addAll(items);
    }

    public void clear() {
        if (mData == null) {
            return;
        }
        mData.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<T> items) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public T getItem(int position) {
        if (mData == null || position >= mData.size() || position < 0) {
            return null;
        }
        return mData.get(position);
    }

    public int getPosition(T item) {
        if (mData == null || mData.isEmpty()) {
            return -1;
        }
        return mData.indexOf(item);
    }

    public T getLast() {
        if (mData == null) {
            return null;
        }
        return getItem(mData.size() - 1);
    }

    @Override
    public CustomViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CustomViewHolder<>(getBinding(parent, inflater));
    }

    /**
     * 获取Binding
     *
     * @param parent   parent
     * @param inflater inflater
     * @return Binding
     */
    public abstract B getBinding(ViewGroup parent, LayoutInflater inflater);

    @Override
    public void onBindViewHolder(final CustomViewHolder<B> holder, final int position) {
        final T item = getItem(position);
        if (item != null) {
            bindItem(holder.mBinding, item, position);
            setClickListener(holder, item);
        }
    }

    protected void setClickListener(final CustomViewHolder<B> holder, final T item) {
        if (mListener == null) {
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view, item, holder.getAdapterPosition());
            }
        });
    }

    public abstract void bindItem(B binding, T item, int position);

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mListener = listener;
    }

    public void addItem(T item) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
    }

    public void addItem(int position, T item) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public T removeItem(int position) {
        if (mData == null || position < 0 || position >= mData.size()) {
            return null;
        }
        T item = mData.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    final public int getRealCount() {
        return mData == null ? 0 : mData.size();
    }

    final public boolean isEmpty() {
        return mData == null || mData.isEmpty();
    }
}
