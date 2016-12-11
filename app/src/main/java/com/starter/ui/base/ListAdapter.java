package com.starter.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

abstract public class ListAdapter<ViewHolderType extends RecyclerView.ViewHolder, ModelType>
    extends RecyclerView.Adapter<ViewHolderType> implements View.OnClickListener {
    protected List<ModelType> dataList = new ArrayList<>();
    protected OnRecyclerViewItemClickListener<ModelType> itemClickListener;

    public void add(ModelType dataItem, int pos) {
        dataList.add(pos, dataItem);

        notifyItemInserted(pos);
    }

    public void update(ModelType dataItem) {
        int position = dataList.indexOf(dataItem);

        if (position != -1) {
            dataList.set(position, dataItem);

            notifyItemChanged(position);
        }
    }

    public void update(ModelType dataItem, int position) {
        dataList.set(position, dataItem);

        notifyItemChanged(position);
    }

    public void addAll(List<ModelType> dataList) {
        this.dataList = dataList;

        notifyDataSetChanged();
    }

    public void remove(ModelType dataItem) {
        int pos = dataList.indexOf(dataItem);
        boolean removed = dataList.remove(dataItem);

        if (removed) {
            notifyItemRemoved(pos);
        }
    }

    @Override
    abstract public ViewHolderType onCreateViewHolder(ViewGroup parent, int viewType);

    public List<ModelType> getDataList() {
        return dataList;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            ModelType model = (ModelType) view.getTag();

            itemClickListener.onItemClick(view, model, dataList.indexOf(model));
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener<ModelType> listener) {
        this.itemClickListener = listener;
    }
}