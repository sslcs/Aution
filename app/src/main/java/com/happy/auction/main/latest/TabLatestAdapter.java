package com.happy.auction.main.latest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happy.auction.R;
import com.happy.auction.main.latest.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 最新成交Adapter
 */
public class TabLatestAdapter extends RecyclerView.Adapter<TabLatestAdapter.ViewHolder> {

    private List<DummyItem> mValues;

    public TabLatestAdapter(List<DummyItem> items) {
        addAll(items);
    }

    public void clear() {
        mValues.clear();
    }

    public void addAll(List<DummyItem> items) {
        if (mValues == null) {
            mValues = new ArrayList<>();
        }
        mValues.addAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_latest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(position));
        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        DummyItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
