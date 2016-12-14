package com.starter.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

abstract public class ListAdapter<V extends RecyclerView.ViewHolder, M>
    extends RecyclerView.Adapter<V> implements View.OnClickListener {
    protected List<M> dataList = new ArrayList<>();
    protected OnRecyclerViewItemClickListener<M> itemClickListener;

    public void add(M dataItem, int pos) {
        dataList.add(pos, dataItem);

        notifyItemInserted(pos);
    }

    public void update(M dataItem) {
        int position = dataList.indexOf(dataItem);

        if (position != -1) {
            dataList.set(position, dataItem);

            notifyItemChanged(position);
        }
    }

    public void update(M dataItem, int position) {
        dataList.set(position, dataItem);

        notifyItemChanged(position);
    }

    public void addAll(List<M> dataList) {
        this.dataList = dataList;

        notifyDataSetChanged();
    }

    public void remove(M dataItem) {
        int pos = dataList.indexOf(dataItem);
        boolean removed = dataList.remove(dataItem);

        if (removed) {
            notifyItemRemoved(pos);
        }
    }

    @Override
    abstract public V onCreateViewHolder(ViewGroup parent, int viewType);

    public List<M> getDataList() {
        return dataList;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            M model = (M) view.getTag();

            itemClickListener.onItemClick(view, model, dataList.indexOf(model));
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener<M> listener) {
        this.itemClickListener = listener;
    }
}