package com.starter.ui.base;

import android.view.View;

public interface OnRecyclerViewItemClickListener<M> {
    void onItemClick(View view, M model, int position);
}